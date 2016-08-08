package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This class is a sightly provider used in page intro component.
 *
 * @author bharath.kambam
 *
 */
public class PageIntroSightlyProvider extends WCMUsePojo {

	/**
	 * Page Intro Title.
	 */
	private String pageIntroTitle;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.adobe.cq.sightly.WCMUsePojo#activate()
	 */
	@Override
	public void activate() throws Exception {
		pageIntroTitle = fetchPageIntroTitle();
	}

	/**
	 * Fetch the page intro title.
	 */
	private String fetchPageIntroTitle() {
		return getCurrentPage().getNavigationTitle() != null ? getCurrentPage().getNavigationTitle()
				: getCurrentPage().getTitle();
	}

	/**
	 * Get the page title intro.
	 *
	 * @return the pageIntroTitle
	 */
	public String getPageIntroTitle() {
		return pageIntroTitle;
	}

}
