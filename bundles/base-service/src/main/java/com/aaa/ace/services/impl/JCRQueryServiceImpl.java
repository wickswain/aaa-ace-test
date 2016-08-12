package com.aaa.ace.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.JCRQueryService;

/**
 * @author yogesh.mahajan
 *
 */

@Component
@Service(JCRQueryService.class)

public class JCRQueryServiceImpl implements JCRQueryService {
	
	public static final Logger log = LoggerFactory.getLogger(JCRQueryServiceImpl.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory;
	
	Session session;
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Node> getResults(String queryStatement, int numOfResults) throws RepositoryException
	{
		
		log.info("Started getResults");
		
		List<Node> answerList = new ArrayList<Node>(); 
		
		ResourceResolver resourceResolver = null;
		
		try {
			//TODO: remove the admin session and use the service session, uncomment below section
			//keep it for demo to avoid need of additional service mapper configuration 
			resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
			/*
			Map<String, Object> paramMap = new HashMap<String, Object>();	
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "jcrqueryservice");
			resourceResolver = resolverFactory.getServiceResourceResolver(paramMap);
			*/
			
		} catch (LoginException e) {

			log.error("Exception in getting resouce resolver", e);
			return null;
		}
		
		session = resourceResolver.adaptTo(Session.class);
		
	    //Obtain the query manager for the session ...
	    javax.jcr.query.QueryManager queryManager = session.getWorkspace().getQueryManager();
	    
		
	    log.info("Creating query with statement {}", queryStatement);
	    
	    javax.jcr.query.Query query = queryManager.createQuery(queryStatement,"JCR-SQL2");

	    if(numOfResults > 0)
	    {
	    	query.setLimit(numOfResults);
	    }
	    
	    //Execute the query and get the results ...
	    javax.jcr.query.QueryResult result = query.execute();
	    
	    //Iterate over the nodes in the results ...
	    javax.jcr.NodeIterator nodeIter = result.getNodes();
	     
	    log.info("Query returned iterator of size {}", nodeIter.getSize());
	    
	    while ( nodeIter.hasNext() ) {

	    	Node node = nodeIter.nextNode();
	    	answerList.add(node);
	      }
		
	    log.info("Returning list of size {}", answerList.size());
	    
		return answerList;
	}
	

}
