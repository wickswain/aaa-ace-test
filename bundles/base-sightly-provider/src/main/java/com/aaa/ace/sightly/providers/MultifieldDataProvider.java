package com.aaa.ace.sightly.providers;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to get the child resource properties of current
 * resource.
 *
 */
public class MultifieldDataProvider extends WCMUsePojo {
	private static final Logger log = LoggerFactory.getLogger(MultifieldDataProvider.class);

	/**
	 * child resource variable.
	 */
	private Resource childResource;

	@Override
	public void activate() throws Exception {
		String fieldset = get("name", String.class);
		childResource = getResource().getChild(fieldset).getChild("columnfieldset");
		log.debug("childResource" + childResource);
	}

	/**
	 * gets the child resource matching with the name passed as parameter.
	 *
	 * @return Resource
	 */
	public Resource getChildResource() {
		return childResource;
	}

}
