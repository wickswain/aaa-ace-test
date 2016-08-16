package com.aaa.ace.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.QueryService;

/**
 * @author yogesh.mahajan
 *
 */

@Component
@Service(QueryService.class)

public class JCRQueryServiceImpl implements QueryService {
	
	public static final Logger log = LoggerFactory.getLogger(JCRQueryServiceImpl.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory;
	
	ResourceResolver resourceResolver = null;
	
	Session session;
	
    @Activate
    public void activate(final Map<String, Object> props) {
    	
		try {
			
			Map<String, Object> paramMap = new HashMap<String, Object>();	
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "aaagenericservice");
			resourceResolver = resolverFactory.getServiceResourceResolver(paramMap);
			
		} catch (LoginException e) {

			log.error("Exception in getting resouce resolver", e);
		}
		
		session = resourceResolver.adaptTo(Session.class);
    }
	
	
	@Override
	public List<Node> getResultNodes(String queryStatement, int numOfResults) throws RepositoryException
	{
		
		log.info("Started getResults");
		
		List<Node> answerList = new ArrayList<Node>(); 
		
		NodeIterator nodeIter = executeQuery(queryStatement, numOfResults);
	    
	    while ( nodeIter.hasNext() ) {

	    	Node node = nodeIter.nextNode();
	    	answerList.add(node);
	      }
		
	    log.info("Returning list of size {}", answerList.size());
	    
		return answerList;
	}
	
	@Override
	public List<Resource> getResultResources(String queryStatement, int numOfResults) throws RepositoryException
	{
		List<Resource> answerResourceList = new ArrayList<Resource>(); 
		
		NodeIterator nodeListResult = executeQuery(queryStatement, numOfResults);
		
	    while ( nodeListResult.hasNext() ) {

	    	Node node = nodeListResult.nextNode();
	    	answerResourceList.add(resourceResolver.getResource(node.getPath()));
	      }
		
		return answerResourceList;
		
	}
	
	private NodeIterator executeQuery(String queryStatement, int numOfResults) throws RepositoryException
	{
		if (null == session)
		{
			log.error("Session is null, could not fetch the results ");
			return null;
		}
		
		//Obtain the query manager for the session ...
	    QueryManager queryManager = session.getWorkspace().getQueryManager();
	    
		
	    log.info("Creating query with statement {}", queryStatement);
	    
	    Query query = queryManager.createQuery(queryStatement,"JCR-SQL2");

	    if(numOfResults > 0)
	    {
	    	query.setLimit(numOfResults);
	    }
	    
	    //Execute the query and get the results ...
	    QueryResult result = query.execute();
	    
	    //Iterate over the nodes in the results ...
	    NodeIterator nodeIter = result.getNodes();
	     
	    log.info("Query returned iterator of size {}", nodeIter.getSize());
	    
	    return nodeIter;
	}

}
