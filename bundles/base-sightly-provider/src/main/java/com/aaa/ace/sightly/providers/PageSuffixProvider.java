package com.aaa.ace.sightly.providers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.PageSuffixResolverService;
import com.adobe.cq.sightly.WCMUsePojo;

public class PageSuffixProvider extends WCMUsePojo{
	
	private static final Logger log = LoggerFactory.getLogger(PageSuffixProvider.class);
	private String url1;
	private String url2;

	@Override
	public void activate() throws Exception {
		url1 = getProperties().get("link1URL", String.class);
		log.debug("url1 inside PageSuffixProvider class: " + url1);
		url2 = getProperties().get("link2URL", String.class);
		log.debug("url2 inside PageSuffixProvider class: " + url2);
		url1 = addPageSuffix(url1);
		url2 = addPageSuffix(url2);
	}
	
	private String addPageSuffix(String url){
		if (StringUtils.isNotBlank(url) && url.startsWith("/content")) {
			PageSuffixResolverService pageService = getSlingScriptHelper().getService(PageSuffixResolverService.class);
			return pageService.resolveLinkURL(getResourceResolver(), url);
		}
		return url;
	}
	
	/**
	 * @return the url1
	 */
	public String getUrl1() {
		return url1;
	}
	
	/**
	 * @return the url2
	 */
	public String getUrl2() {
		return url2;
	}

}
