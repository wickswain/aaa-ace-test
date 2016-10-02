package com.aaa.ace.services;

import java.util.List;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

/**
 * Service interface for creating duplicate component instances. 
 * This implementation should encapsulate the logic for generating duplicate instances of a component.
 * 
 * @author manish.singh
 *
 */
public interface MultipleComponentProviderService {
	
	List<Resource> createComponent(Resource resource, int compCount, String nodeType, String nodePath, String resourceName) throws PersistenceException;

}
