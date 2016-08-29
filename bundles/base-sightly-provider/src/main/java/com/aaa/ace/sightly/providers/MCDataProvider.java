package com.aaa.ace.sightly.providers;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * Utility class to help a Sightly component to get a child node's properties of
 * current resource.
 */

public class MCDataProvider extends WCMUsePojo {
	private static final Logger log = LoggerFactory.getLogger(MCDataProvider.class);

	private Resource childResource;

	@Override
	public void activate() throws Exception {
		log.info("Start of MCDataProvider class");
		String childNode = get("child", String.class);
		childResource = getResource().getChild(childNode);
		log.debug("Child resource path: " + childResource.getPath());
	}

	public Resource getChildResource() {
		return childResource;
	}

}
