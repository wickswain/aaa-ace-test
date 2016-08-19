package com.aaa.ace.sightly.providers;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
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
	private static final String EQUALS_CHARACTER = "=";
	private static final String AMPERSAND_CHARACTER = "&";
	private static final String EXCLAMATION_CHARACTER = "?";
	private static final String DELIMITER = ",";
	private static final String DOMAIN_PLACE_HOLDER = "[Domain]";
	private static final String CLUB_PLACE_HOLDER = "[ClubName]";
	private static final String ENVIRONMENT_PROPERTY_NAME = "environment";

	private String url;

	@Override
	public void activate() throws Exception {
		log.info("Start of CTANavigationURLProvider class");
		String keys = get("queryStringKey", String.class);
		Resource childResource = get("childResource", Resource.class);

		String[] keyArray = null;
		Value[] selectedCommonQueryStringKeyArray = {};

		if (childResource != null) {
			log.debug("Child resource path: " + childResource.getPath());
			Node propertiesNode = childResource.adaptTo(Node.class);
			
			if (propertiesNode.hasProperty("selectedQueryString")) {
				Property selectedCommonQuery = propertiesNode.getProperty("selectedQueryString");

				if (selectedCommonQuery != null) {
					if (selectedCommonQuery.isMultiple()) {
						selectedCommonQueryStringKeyArray = selectedCommonQuery.getValues();
					} else {
						Value[] commonQuery = {selectedCommonQuery.getValue()};
						selectedCommonQueryStringKeyArray = commonQuery;
					}
				}
			}
		}

		if (StringUtils.isNotBlank(keys) && StringUtils.isNotEmpty(keys))
			keyArray = keys.split(CTANavigationURLProvider.DELIMITER);
		url = get("url", String.class);

		if (url != null && url.startsWith("/content")) {
			PageSuffixResolverService pageService = getSlingScriptHelper().getService(PageSuffixResolverService.class);
			url = pageService.resolveLinkURL(getResourceResolver(), url);
		}

		RunmodeProviderService runmodeService = getSlingScriptHelper().getService(RunmodeProviderService.class);
		String runmode = runmodeService.getEnvironmentInfo();
		String env = StringUtils.isNotBlank(runmode) ? runmode : get(ENVIRONMENT_PROPERTY_NAME, String.class);
		RegionDataService regionService = getSlingScriptHelper().getService(RegionDataService.class);
		String regionNameFromURL = regionService.getRegionInfo(getRequest());
		String regionName = StringUtils.isNotBlank(regionNameFromURL) ? regionNameFromURL
				: get("regionName", String.class);
		log.info("custom queryStringKeys from component: " + keys);

		if (StringUtils.isNotBlank(url) && StringUtils.isNotEmpty(url)) {
			String appURL = getExternalAppURL(url, regionName, env);
			url = appURL;
			if (keyArray != null) {
				for (int i = 0; i < keyArray.length; i++) {
					if (url.contains(CTANavigationURLProvider.DOMAIN_PLACE_HOLDER)
							&& url.contains(CTANavigationURLProvider.CLUB_PLACE_HOLDER)) {
						if (StringUtils.isNotBlank(keyArray[i].trim()))
							url = getQueryStringValueConcatenatedURL(keyArray[i].trim(), appURL);
					} else
						url = getQueryStringValueConcatenatedURL(keyArray[i].trim(), url);
				}
			}

			if (selectedCommonQueryStringKeyArray.length > 0) {
				for (int i = 0; i < selectedCommonQueryStringKeyArray.length; i++) {
					if (url.contains(CTANavigationURLProvider.DOMAIN_PLACE_HOLDER)
							&& url.contains(CTANavigationURLProvider.CLUB_PLACE_HOLDER)) {
						if (StringUtils.isNotBlank(selectedCommonQueryStringKeyArray[i].toString().trim()))
							url = getQueryStringValueConcatenatedURL(
									selectedCommonQueryStringKeyArray[i].toString().trim(), appURL);
					} else
						url = getQueryStringValueConcatenatedURL(selectedCommonQueryStringKeyArray[i].toString().trim(),
								url);
				}
			}
		}

		log.info("End of CTANavigationURLProvider class");
	}

	private String getQueryStringValueConcatenatedURL(String key, String url) {
		log.info("Start of getQueryStringValueConcatenatedURL method");
		if (StringUtils.isNotBlank(key)) {
			if (getRequest().getParameter(key) != null) {
				String queryStringValue = getRequest().getParameter(key);
				log.info("queryStringValue: " + queryStringValue);
				url = (url.contains(CTANavigationURLProvider.EXCLAMATION_CHARACTER)
						? url.concat(CTANavigationURLProvider.AMPERSAND_CHARACTER)
						: url.concat(CTANavigationURLProvider.EXCLAMATION_CHARACTER)).concat(key)
								.concat(CTANavigationURLProvider.EQUALS_CHARACTER).concat(queryStringValue);
			} else
				log.info("No Query String " + key + " found in the authored URL " + url);
		}
		log.info("Query String Value Concatenated Final URL: " + url);
		return url;
	}

	public String getExternalAppURL(String url, String regionName, String env) {
		log.info("Start of getExternalAppURL method");
		log.info("URL: " + url);
		log.info("Region: " + regionName);
		log.info("Env: " + env);
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(regionName) && StringUtils.isNotBlank(env))
			return url.replace(CTANavigationURLProvider.DOMAIN_PLACE_HOLDER, env)
					.replace(CTANavigationURLProvider.CLUB_PLACE_HOLDER, regionName);
		else {
			log.error("Error: Either URL or RegionName or Env received from component is empty or null");
			return null;
		}
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
}