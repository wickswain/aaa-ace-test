package com.aaa.ace.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import com.aaa.ace.services.MultipleComponentProviderService;

@Component
@Service(MultipleComponentProviderService.class)
@Properties({ @Property(name = "service.vendor", value = "com.aaa.ace.services"),
        @Property(name = "service.description", value = "com.aaa.ace.services Duplicate component instances provider Service") })
public class MultipleComponentProviderServiceImpl implements MultipleComponentProviderService{

	@Override
	public List<Resource> createComponent(Resource resource, int compCount, String nodeType, String nodePath, String resourceName) throws PersistenceException{
		List<Resource> resourceList = new ArrayList<>();
		ResourceResolver resourceResolver = resource.getResourceResolver();
        Map<String,Object> componentProperties = new HashMap<>();
        componentProperties.put(nodeType, nodePath);
        Resource childResource = null;
        for (int i = 0; i < compCount; i++) {
        	String childResourceName = resourceName + String.valueOf(i+1);
        	childResource = resource.getChild(childResourceName);
		   
		    if(childResource == null) {
		    	childResource = resourceResolver.create(resource, childResourceName, componentProperties);
		    	resourceResolver.commit();
		    }
		    resourceList.add(childResource);
		}
		return resourceList;
	}
}
