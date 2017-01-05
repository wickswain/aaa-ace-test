package com.aaa.ace.sightly.providers;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to get the path of the personalized navigation
 * components used under primary navigation.
 *
 */
public class ComponentPersonalizationPathProvider extends WCMUsePojo {

	/**
	 * Navigation components path.
	 */
	private String path;

	@Override
	public void activate() throws Exception {
		String componentPath = get("path", String.class);
		path = getNavComponentPath(getResourceResolver(), componentPath);
	}

	/**
	 * Gets the navigation components path.
	 *
	 * @return Resource
	 */
	public String getPath() {
		return path;
	}

	/*
	 * Validate and return valid path.
	 */
	private String getNavComponentPath(ResourceResolver resolver, String componentPath) {
		String path = componentPath;
		Resource componentResource = resolver.resolve(componentPath + "/default");

		if (componentResource != null && !ResourceUtil.isNonExistingResource(componentResource)) {
			path = componentResource.getPath();
		}

		return path;
	}

}
