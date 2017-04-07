package com.aaa.ace.sightly.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider class is used to fetch the template path from the
 * current page.
 * 
 */
public class TemplateUtilsProvider extends WCMUsePojo {

	private static final Logger log = LoggerFactory.getLogger(TemplateUtilsProvider.class);

	/**
	 * Error page template path variable.
	 */
	private static String errorPageTemplatePath = "/apps/aaa-core/templates/errorpage";
	private String currentPageTemplatePath;
	private boolean errorPageFlag = false;

	@Override
	public void activate() throws Exception {
		currentPageTemplatePath = getCurrentPage().getProperties().get("cq:template", "");
		log.debug("Current page template path : " + currentPageTemplatePath);
		if (currentPageTemplatePath != null) {
			errorPageFlag = errorPageTemplatePath.equals(currentPageTemplatePath);
		}
	}

	/**
	 * gets the current page template path
	 *
	 * @return currentPageTemplatePath
	 */
	public String getCurrentPageTemplatePath() {
		return currentPageTemplatePath;
	}

	/**
	 * gets the errorPageFlag
	 *
	 * @return errorPageFlag
	 */
	public boolean isErrorPageFlag() {
		return errorPageFlag;
	}

}
