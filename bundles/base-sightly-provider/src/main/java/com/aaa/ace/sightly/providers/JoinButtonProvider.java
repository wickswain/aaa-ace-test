package com.aaa.ace.sightly.providers;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to get the Join-Button path of Global header component.
 *
 */
public class JoinButtonProvider extends WCMUsePojo {

	/**
	 * Logger object
	 */
	private static final Logger log = LoggerFactory.getLogger(JoinButtonProvider.class);

	/**
	 * GLOBAL_HEADER_PATH variable
	 */
	private static String GLOBAL_HEADER_PATH = "/content/ace-www/en/jcr:content/header-ipar/global_header";

	/**
	 * Join-Button resource of Global header component.
	 */
	private Resource joinButtonResource;

	@Override
	public void activate() throws Exception {
		log.debug("Start of JoinButtonProvider class");

		String resourcePath = getResource().getPath();
		if (GLOBAL_HEADER_PATH.equals(resourcePath)) {
			joinButtonResource = getResource().getChild("cta-small").getChild("button1");
		}
		log.debug("End of JoinButtonProvider class");
	}

	/**
	 * @return joinButtonResource
	 */
	public Resource getJoinButtonResource() {
		return joinButtonResource;
	}
}
