package com.aaa.ace.sightly.providers;

import com.aaa.ace.services.PageSuffixResolverService;
import com.adobe.cq.sightly.WCMUsePojo;

public class FAQProvider extends WCMUsePojo{
	
	private String url1;
	private String url2;

	@Override
	public void activate() throws Exception {
		url1 = get("url1", String.class);
		url2 = get("url2", String.class);
		url1 = addPageSuffix(url1);
		url2 = addPageSuffix(url2);
	}
	
	private String addPageSuffix(String url){
		if (url != null && url.startsWith("/content")) {
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
