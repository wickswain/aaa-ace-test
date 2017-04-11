package com.aaa.ace.workflow.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.common.Constants;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.AssetReferenceSearch;
import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentFilter;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;

/**
 * Publish the page content, assets, and campaign references to a specific
 * replication agent (review) on specific server.
 *
 * @author bharath.kambam
 *
 */
@Component(metatype = false, immediate = false)
@Properties({ @Property(name = "service.label", value = "Publish Page Workflow Process"),
		@Property(name = "service.description", value = "This workflow process replicate the content, asset and campaign references to specific replication agent configured on workflow arguments."),
		@Property(label = "Workflow Label", name = "process.label", value = "Publish Page Workflow Process", description = "This workflow process replicate the content, asset and campaign references to specific replication agent configured on workflow arguments.") })
@Service
public class PublishPageProcess implements WorkflowProcess {

	/**
	 * Campaigns path workflow argument property.
	 */
	private static final String CAMPAIGNS_PATH_PROPERTY = "path";

	/**
	 * Replication agent ID workflow argument property.
	 */
	private static final String REPLICATION_AGENT_ID_PROPERTY = "agentId";

	/**
	 * Logger variable.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PublishPageProcess.class);

	/**
	 * Resource resolver variable.
	 */
	private ResourceResolver resolver = null;

	/**
	 * Campaigns Path variable.
	 */
	private String campaignsPath;

	@Reference
	private Replicator replicator;

	@Reference
	private QueryBuilder builder;

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
		logger.info("PublishPageReviewProcess execute method starts.");

		String payloadPath = null;
		Session adminSession = null;
		String agentID = null;

		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// Mention the subServiceName you had used in the User Mapping
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "workflowmapperservice");

			resolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
			adminSession = resolver.adaptTo(Session.class);

			// Fetch the arguments from workflow meta data.
			String processArgs = args.get(Constants.PROCESS_ARGS, String.class);
			String[] argumnets = processArgs.split(Constants.COMMA);
			logger.debug("Workflow process step arguments: " + processArgs);

			// Split and separate the arguments received from workflow meta
			// data.
			for (String argumnet : argumnets) {
				String[] argumentNameValue = argumnet.split(Constants.COLON);

				if (argumentNameValue[0].equals(CAMPAIGNS_PATH_PROPERTY)) {
					campaignsPath = argumentNameValue[1];
				} else if (argumentNameValue[0].equals(REPLICATION_AGENT_ID_PROPERTY)) {
					agentID = argumentNameValue[1];
				}
			}
			logger.debug("Campigns path: " + campaignsPath);

			// Get the payload path from workflow data.
			WorkflowData workflowData = workItem.getWorkflowData();

			if (workflowData.getPayloadType().equals(Constants.JCR_PATH)) {
				List<String> replicatePaths = new ArrayList<String>();
				payloadPath = workflowData.getPayload().toString();

				logger.debug("Replicate paths count Before: ", replicatePaths.size());

				// replicate the reference assets in page
				/*
				 * Map<String, Asset> assetReferences =
				 * getAssetReferencesInPage(payloadPath, adminSession);
				 * log.info("Asset references for Page {} are {}.", payloadPath,
				 * assetReferences.entrySet().toString()); for (Entry<String,
				 * Asset> assetReference : assetReferences.entrySet()) {
				 * replicatePaths.add(assetReference.getValue().getPath()); }
				 */

				// replicate the reference campaigns in page
				List<Hit> campaignReferences = getCampaignReferencesInPage(payloadPath, adminSession);
				logger.debug("Campaigns references for Page {} are {}.", payloadPath, campaignReferences.toString());

				if (campaignReferences != null) {
					for (Hit hit : campaignReferences) {
						replicatePaths.add(hit.getPath());
					}
				}
				logger.info("Workflow payload path: " + payloadPath);
				replicatePaths.add(payloadPath);

				logger.debug("Replicate paths count: ", replicatePaths.size());

				if (agentID != null) {
					ReplicationOptions replicationOpts = getReplicationOptions(agentID);
					logger.debug("Replication Options in WF: " + replicationOpts.toString());

					for (String replicatePath : replicatePaths) {
						replicator.replicate(adminSession, ReplicationActionType.ACTIVATE, replicatePath,
								replicationOpts);
					}
				} else {
					for (String replicatePath : replicatePaths) {
						replicator.replicate(adminSession, ReplicationActionType.ACTIVATE, replicatePath);
					}
				}

				logger.debug("Page {} replicated successfully.", payloadPath);
			}

		} catch (LoginException e) {
			logger.error("Error occured while fetching the resource resolver from workflow session.");
			e.printStackTrace();
		} catch (ReplicationException e) {
			logger.error("Error occured while replicating the resource from workflow.");
			e.printStackTrace();
		} catch (RepositoryException e) {
			logger.error("Error occured while replicating the campaigns resource from workflow.");
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Error occured while replicating resource from workflow.", e.getMessage());
			e.printStackTrace();
		} finally {
			if (adminSession != null) {
				adminSession.logout();
			}
		}

		logger.info("PublishPageReviewProcess execute method ends.");
	}

	/**
	 * Gets the replication options object.
	 *
	 * @return
	 */
	private ReplicationOptions getReplicationOptions(final String agentID) {
		logger.info("PublishPageReviewProcess getReplicationOptions method starts.");
		logger.debug("Replication Agent in WF: " + agentID);

		ReplicationOptions replicationOpts = new ReplicationOptions();

		replicationOpts.setFilter(new AgentFilter() {
			public boolean isIncluded(final Agent agent) {
				logger.debug("Replication Agent in AEM: " + agent.getId());
				return agentID.equals(agent.getId());
			}
		});

		replicationOpts.setSuppressVersions(true);
		replicationOpts.setSuppressStatusUpdate(true);

		logger.info("PublishPageReviewProcess getReplicationOptions method ends.");
		return replicationOpts;
	}

	public List<Hit> getCampaignReferencesInPage(final String pagePath, final Session adminSession) {
		logger.info("PublishPageReviewProcess getCampaignReferencesInPage method starts.");

		List<Hit> campaignResults = null;
		Resource resource = resolver.getResource(pagePath).adaptTo(Page.class).getContentResource();

		logger.debug("Campaign references to be retrieved for page {} ", pagePath);
		if (resource.hasChildren()) {
			Iterator<Resource> childs = resource.listChildren();
			while (childs.hasNext()) {
				Resource childResource = (Resource) childs.next();

				List<Hit> results = getTargetedComponentResults(childResource.getPath(), adminSession);
				logger.debug("Campaign references retrieved for page {} on resource {} count is: {}", pagePath,
						childResource.getPath(), results.size());

				if (results != null) {
					if (campaignResults == null) {
						campaignResults = results;
					} else {
						campaignResults.addAll(results);
					}
				}
			}
		}
		logger.debug("Campaign references retrieved for page {} total count is: {}", pagePath, campaignResults.size());

		logger.info("PublishPageReviewProcess getCampaignReferencesInPage method ends.");
		return campaignResults;
	}

	private List<Hit> getTargetedComponentResults(final String pagePath, final Session adminSession) {
		logger.info("PublishPageReviewProcess getTargetedComponentResults method starts.");

		List<Hit> campaignResults = new ArrayList<Hit>();
		Map<String, String> map = new HashMap<String, String>();

		map.put("path", pagePath);
		map.put("path.exact", "true");
		map.put("type", "nt:unstructured");
		map.put("property", "location");
		map.put("property.operation", "exists");

		logger.debug("Query builder map properties: " + map.toString());
		// Create a Query instance
		Query query = builder.createQuery(PredicateGroup.create(map), adminSession);

		// Get the query results
		SearchResult result = query.getResult();
		logger.debug("Query builder results Hits size: " + result.getHits().size());

		if (result.getHits().size() > 0) {
			Iterator<Resource> resultResources = result.getResources();
			while (resultResources.hasNext()) {
				Resource resource = (Resource) resultResources.next();
				String location = resource.getValueMap().get("location", "");

				if (StringUtils.isNotBlank(location)) {
					List<Hit> results = getCampaignResults(location, adminSession);
					if (results != null) {
						for (Hit hit : results) {
							campaignResults.add(hit);
						}
					}
				}
			}
		}

		logger.info("PublishPageReviewProcess getTargetedComponentResults method ends.");
		return campaignResults;
	}

	private List<Hit> getCampaignResults(final String location, final Session adminSession) {
		logger.info("PublishPageReviewProcess getCampaignResults method starts.");

		List<Hit> results = null;
		Map<String, String> map = new HashMap<String, String>();

		if (StringUtils.isBlank(campaignsPath)) {
			campaignsPath = "/content/campaigns/ace-www";
		}

		map.put("path", campaignsPath);
		map.put("type", "cq:PageContent");
		map.put("property", "location");
		map.put("property.value", location);
		map.put("property.operation", "like");

		logger.debug("Query builder map properties: " + map.toString());
		// Create a Query instance
		Query query = builder.createQuery(PredicateGroup.create(map), adminSession);

		// Get the query results
		SearchResult result = query.getResult();
		logger.debug("Query builder results Hits size: " + result.getHits().size());

		if (result.getHits().size() > 0) {
			results = result.getHits();
		}

		logger.info("PublishPageReviewProcess getCampaignResults method ends.");
		return results;
	}

	public Map<String, Asset> getAssetReferencesInPage(final String pagePath, final Session adminSession)
			throws RepositoryException {
		logger.info("PublishPageReviewProcess getAssetReferencesInPage method starts.");

		Resource resource = resolver.getResource(pagePath).adaptTo(Page.class).getContentResource();
		AssetReferenceSearch referenceSearch = new AssetReferenceSearch(resource.adaptTo(Node.class),
				DamConstants.MOUNTPOINT_ASSETS, resolver);
		logger.debug("Assets references to be retrieved for page {} ", resource.getPath());

		if (referenceSearch.search().size() > 0) {
			logger.debug("{} assets references on page {} ", referenceSearch.search().size(), pagePath);
			return referenceSearch.search();
		}

		logger.info("PublishPageReviewProcess getAssetReferencesInPage method ends.");
		return null;
	}

}
