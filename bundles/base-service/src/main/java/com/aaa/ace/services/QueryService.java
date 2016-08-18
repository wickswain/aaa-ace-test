package com.aaa.ace.services;

import org.apache.sling.api.resource.Resource;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Generic service interface to fetch results in node list form 
 * or resource list form from JCR.
 * 
 * 
 * @author yogesh.mahajan
 *
 */
public interface QueryService {

    /** 
     * Get the results in node list.
     */
    List<Node> getResultNodes(String queryStatement, int numOfResults) throws RepositoryException;

    /**
     * Get the results in Resource list.
     * 
     * @param queryStatement
     * @param numOfResults
     * @return
     * @throws RepositoryException
     */
    List<Resource> getResultResources(String queryStatement, int numOfResults)
            throws RepositoryException;

}
