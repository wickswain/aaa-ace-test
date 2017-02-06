package com.aaa.ace.workflow.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.aaa.ace.services.AEMUtilityService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.AssetReferenceSearch;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;

/**
 * Publish the page content and it's asset, campaigns reference
 *
 * @author bharath.kambam
 *
 */
@Component(metatype = false, immediate = false)
@Properties({ @Property(name = "service.label", value = "Publish Page Workflow Process"),
		@Property(name = "service.description", value = "Publish Page Workflow Process implementation."),
		@Property(label = "Workflow Label", name = "process.label", value = "Publish Page Workflow Process", description = "Publish Page Workflow Process description") })
@Service
public class PublishPageProcess implements WorkflowProcess {

	private static final Object CAMPAIGNS_PATH_PROPERTY = "path";

	/** Default log. **/
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private ResourceResolver resolver = null;

	private String campaignsPath;

	@Reference
	Replicator replicator;

	@Reference
	QueryBuilder builder;

	@Reference
	AEMUtilityService utilityService;

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
		String payloadPath = null;
		Session adminSession = null;

		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// Mention the subServiceName you had used in the User Mapping
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "workflowmapperservice");

			resolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
			adminSession = resolver.adaptTo(Session.class);

			// Fetch the arguments from workflow meta data.
			String processArgs = args.get(Constants.PROCESS_ARGS, String.class);
			String[] argumnets = processArgs.split(Constants.COMMA);
			log.info("Workflow process step arguments: " + processArgs);

			// Split and separate the arguments received from workflow meta
			// data.
			for (String argumnet : argumnets) {
				String[] argumentNameValue = argumnet.split(Constants.COLON);

				if (argumentNameValue[0].equals(CAMPAIGNS_PATH_PROPERTY)) {
					campaignsPath = argumentNameValue[1];
				}
			}

			// Get the payload path from workflow data.
			WorkflowData workflowData = workItem.getWorkflowData();
			if (workflowData.getPayloadType().equals(Constants.JCR_PATH)) {
				List<String> replicatePaths = new ArrayList<String>();
				payloadPath = workflowData.getPayload().toString();
				log.info("Workflow payload path: " + payloadPath);
				replicatePaths.add(payloadPath);

				// replicate the reference assets in page
				/*Map<String, Asset> assetReferences = getAssetReferencesInPage(payloadPath, adminSession);
				log.info("Asset references for Page {} are {}.", payloadPath, assetReferences.entrySet().toString());
				for (Entry<String, Asset> assetReference : assetReferences.entrySet()) {
					replicatePaths.add(assetReference.getValue().getPath());
				}*/

				// replicate the reference campaigns in page
				List<Hit> campaignReferences = getCampaignReferencesInPage(payloadPath, adminSession);
				log.info("Campaigns references for Page {} are {}.", payloadPath, campaignReferences.toString());
				for (Hit hit : campaignReferences) {
					replicatePaths.add(hit.getPath());
				}

				for (String replicatePath : replicatePaths) {
					replicator.replicate(adminSession, ReplicationActionType.ACTIVATE, replicatePath);
				}
				log.info("Page {} replicated successfull.", payloadPath);

			}

		} catch (LoginException e) {
			log.error("Error occured while fetching the resource resolver from workflow session.");
			e.printStackTrace();
		} catch (ReplicationException e) {
			log.error("Error occured while replicating the resource from workflow.");
			e.printStackTrace();
		} catch (RepositoryException e) {
			log.error("Error occured while replicating the campaigns resource from workflow.");
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Error occured while replicating resource from workflow.", e.getMessage());
			e.printStackTrace();
		} finally {
			if (adminSession != null) {
				adminSession.logout();
			}
		}

	}

	public List<Hit> getCampaignReferencesInPage(final String pagePath, final Session adminSession) {
		log.info("Campaign references to be retrieved for page {} ", pagePath);
		List<Hit> campaignResults = null;
		Resource resource = resolver.getResource(pagePath).adaptTo(Page.class).getContentResource();

		if (resource.hasChildren()) {
			Iterator<Resource> childs = resource.listChildren();
			while (childs.hasNext()) {
				Resource childResource = (Resource) childs.next();

				log.info("Campaign references to be retrieved for resource {} ", childResource.getPath());
				List<Hit> results = getTargetedComponentResults(childResource.getPath(), adminSession);

				if (campaignResults == null) {
					campaignResults = results;
				} else {
					campaignResults.addAll(results);
				}
			}
		}

		return campaignResults;
	}

	private List<Hit> getTargetedComponentResults(final String pagePath, final Session adminSession) {
		List<Hit> campaignResults = new ArrayList<Hit>();

		Map<String, String> map = new HashMap<String, String>();

		map.put("path", pagePath);
		map.put("path.exact", "true");
		map.put("type", "nt:unstructured");
		map.put("property", "location");
		map.put("property.operation", "exists");

		log.info("Query builder map properties: " + map.toString());
		// Create a Query instance
		Query query = builder.createQuery(PredicateGroup.create(map), adminSession);

		// Get the query results
		SearchResult result = query.getResult();
		log.info("Query builder results Hits size: " + result.getHits().size());

		if (result.getHits().size() > 0) {
			Iterator<Resource> resultResources = result.getResources();
			while (resultResources.hasNext()) {
				Resource resource = (Resource) resultResources.next();
				log.info("Result resource: " + resource.getPath());
				String location = resource.getValueMap().get("location", "");

				if (StringUtils.isNotBlank(location)) {
					List<Hit> temp = getCampaignResults(location, adminSession);
					for (Hit hit : temp) {
						campaignResults.add(hit);
					}
				}
			}
		}

		return campaignResults;
	}

	private List<Hit> getCampaignResults(final String location, final Session adminSession) {
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

		log.info("Query builder map properties: " + map.toString());
		// Create a Query instance
		Query query = builder.createQuery(PredicateGroup.create(map), adminSession);

		// Get the query results
		SearchResult result = query.getResult();
		log.info("Query builder results Hits size: " + result.getHits().size());

		if (result.getHits().size() > 0) {
			results = result.getHits();
		}

		return results;
	}

	public Map<String, Asset> getAssetReferencesInPage(final String pagePath, final Session adminSession)
			throws RepositoryException {
		Resource resource = resolver.getResource(pagePath).adaptTo(Page.class).getContentResource();
		log.info("Assets references to be retrieved for page {} ", resource.getPath());
		AssetReferenceSearch referenceSearch = new AssetReferenceSearch(resource.adaptTo(Node.class),
				DamConstants.MOUNTPOINT_ASSETS, resolver);

		if (referenceSearch.search().size() > 0) {
			log.info(" {} assets references on page {} ", referenceSearch.search().size(), pagePath);
			return referenceSearch.search();
		}

		return null;
	}

}
