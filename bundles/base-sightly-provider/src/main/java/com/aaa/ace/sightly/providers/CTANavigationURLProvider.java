package com.aaa.ace.sightly.providers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.PageSuffixResolverService;
import com.aaa.ace.services.RegionDataService;
import com.aaa.ace.services.RunmodeProviderService;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * @author manish.singh
 *
 */
public class CTANavigationURLProvider extends WCMUsePojo {

	private static final Logger log = LoggerFactory.getLogger(CTANavigationURLProvider.class);

	private static final String CONTENT_ROOT_PATH = "/content";

	private static final String URL_PROPERTY_NAME = "url";

	private static final String DOMAIN_PLACE_HOLDER = "[Domain]";

	private static final String CLUB_PLACE_HOLDER = "[ClubName]";

	private String url;

	@Override
	public void activate() throws Exception {
		log.debug("Start of CTANavigationURLProvider class");

		url = get(URL_PROPERTY_NAME, String.class);

		// Fetch valid URL.
		if (StringUtils.isNotBlank(url) && url.startsWith(CONTENT_ROOT_PATH)) {
			// Fetch valid internal URL.
			PageSuffixResolverService pageService = getSlingScriptHelper().getService(PageSuffixResolverService.class);

			url = pageService.resolveLinkMapURL(getResourceResolver(), url);
		} else if (StringUtils.isNotBlank(url) && url.contains(DOMAIN_PLACE_HOLDER)
				&& url.contains(CLUB_PLACE_HOLDER)) {
			// Fetch valid AAA APP URL.
			RunmodeProviderService runmodeService = getSlingScriptHelper().getService(RunmodeProviderService.class);
			RegionDataService regionService = getSlingScriptHelper().getService(RegionDataService.class);

			String env = runmodeService.getEnvironmentInfo();
			String regionName = regionService.getRegionInfo(getRequest());

			url = getExternalAppURL(url, regionName, env);
		}

		log.debug("End of CTANavigationURLProvider class");
	}

	/**
	 * Gets the valid AAA APP URL.
	 *
	 * @param url
	 * @param regionName
	 * @param env
	 * @return
	 */
	public String getExternalAppURL(String url, String regionName, String env) {
		log.debug("Start of getExternalAppURL method");
		log.debug("URL: " + url);
		log.debug("Region: " + regionName);
		log.debug("Env: " + env);

		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(regionName) && StringUtils.isNotBlank(env)) {
			return url.replace(CTANavigationURLProvider.DOMAIN_PLACE_HOLDER, env)
					.replace(CTANavigationURLProvider.CLUB_PLACE_HOLDER, regionName);
		} else {
			log.error("Error: Either URL or RegionName or Env received from component is empty or null");
		}

		log.debug("Final URL after replacing the placeholder: " + url);

		return url;
	}

	/**
	 * Gets the final URL.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

}
