package com.aaa.ace.sightly.providers;

import java.util.Iterator;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to get the Join-Button path of Global header component.
 *
 */
public class JoinButtonProvider extends WCMUsePojo {
	private static final Logger log = LoggerFactory.getLogger(JoinButtonProvider.class);

	private static String GLOBAL_HEADER_PATH = "/content/ace-www/en/jcr:content/header-ipar/global_header";

	/**
	 * Join-Button path of Global header component.
	 */
	private Resource joinButtonPath;

	@Override
	public void activate() throws Exception {
		log.debug("Start of JoinButtonProvider class");

		String resource = getResource().getPath();
		if (GLOBAL_HEADER_PATH.equals(resource)) {
			Iterator<Resource> listChildren = getResource().listChildren();
			while (listChildren.hasNext()) {
				Iterator<Resource> listChildren2 = listChildren.next().listChildren();
				while (listChildren2.hasNext()) {
					joinButtonPath = listChildren2.next();
				}
			}
		}
		log.debug("End of JoinButtonProvider class");
	}

	/**
	 * @return joinButtonPath
	 */
	public Resource getJoinButtonPath() {
		return joinButtonPath;
	}
}
