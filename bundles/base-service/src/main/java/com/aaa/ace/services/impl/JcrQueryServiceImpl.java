package com.aaa.ace.services.impl;


import com.aaa.ace.services.QueryService;

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


/**
 * JCR based implementation of Query Service.
 * 
 * @author yogesh.mahajan
 *
 */

@Component
@Service(QueryService.class)
public class JcrQueryServiceImpl implements QueryService {

    public static final Logger log = LoggerFactory.getLogger(JcrQueryServiceImpl.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    ResourceResolver resourceResolver = null;

    Session session = null;

    /**
     * Allocating resolver and session objects to be used for query.
     * 
     * @param props - properties used for activation.
     * @throws LoginException 
     */
    @Activate
    public void activate(final Map<String, Object> props) throws LoginException {

        try {
            
            /* Get resource resolver with system user, making service name for mapper generic
             * so that it can be used at other components as well. 
             */
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, "jcrqueryservice");
            resourceResolver = resolverFactory.getServiceResourceResolver(paramMap);

        } catch (LoginException e) {
            log.error("Exception in getting resouce resolver", e);
        }

        if (null != resolverFactory)
        {
            session = resourceResolver.adaptTo(Session.class);
        }
        
    }

    /**
     * Method to get List of Nodes from query.
     */
    @Override
    public List<Node> getResultNodes(String queryStatement, int numOfResults)
            throws RepositoryException {

        log.debug("Started getResults");

        List<Node> answerList = new ArrayList<Node>();

        NodeIterator nodeIter = executeQuery(queryStatement, numOfResults);

        if (null == nodeIter) {
            log.warn("Could not execute query returning empty list");
            return answerList;
        }

        // Iterate the result and construct node list
        while (nodeIter.hasNext()) {
            Node node = nodeIter.nextNode();
            answerList.add(node);
        }

        log.debug("Returning list of size {}", answerList.size());

        return answerList;
    }

    /**
     * Get Resource list of query results.
     */
    @Override
    public List<Resource> getResultResources(String queryStatement, int numOfResults)
            throws RepositoryException {
        List<Resource> answerResourceList = new ArrayList<Resource>();

        NodeIterator nodeListResult = executeQuery(queryStatement, numOfResults);

        if (null == nodeListResult) {
            log.warn("Could not execute query returning empty list");
            return answerResourceList;
        }

        while (nodeListResult.hasNext()) {
            Node node = nodeListResult.nextNode();
            answerResourceList.add(resourceResolver.getResource(node.getPath()));
        }

        return answerResourceList;

    }

    /**
     * Helper method to execute the query on JCR session.
     * 
     */
    private NodeIterator executeQuery(String queryStatement, int numOfResults)
            throws RepositoryException {
        if (null == session) {
            log.error("Session is null, could not fetch the results ");
            return null;
        }

        // Obtain the query manager for the session ...
        QueryManager queryManager = session.getWorkspace().getQueryManager();

        log.debug("Creating query with statement {}", queryStatement);

        Query query = queryManager.createQuery(queryStatement, "JCR-SQL2");

        if (numOfResults > 0) {
            query.setLimit(numOfResults);
        }

        // Execute the query and get the results ...
        QueryResult result = query.execute();

        // Iterate over the nodes in the results ...
        NodeIterator nodeIter = result.getNodes();

        log.debug("Query returned iterator of size {}", nodeIter.getSize());
        
        
        return nodeIter;
    }

}
