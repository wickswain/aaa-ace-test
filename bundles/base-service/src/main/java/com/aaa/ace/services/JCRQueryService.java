package com.aaa.ace.services;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface JCRQueryService {
	
	List<Node> getResults(String queryStatement, int numOfResults)
			throws RepositoryException;

}
