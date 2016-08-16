package com.aaa.ace.services;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;

public interface QueryService {
	
	List<Node> getResultNodes(String queryStatement, int numOfResults)
			throws RepositoryException;

	List<Resource> getResultResources(String queryStatement, int numOfResults)
			throws RepositoryException;

}
