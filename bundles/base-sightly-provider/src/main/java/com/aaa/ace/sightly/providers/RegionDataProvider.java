package com.aaa.ace.sightly.providers;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;

import com.aaa.ace.services.PersonalizationConfigService;

public class RegionDataProvider extends WCMUsePojo {

	private static final Logger log = LoggerFactory.getLogger(RegionDataProvider.class);
	private static final String JCR_CONTENT_PAR_PATH = "/jcr:content/content-par/";

	public String hostName;
	public String regionDisplayName;
	public Resource componentResource;

	@Override
	public void activate() throws Exception {
		log.info("Start of Region Data Provider class");

		PersonalizationConfigService configService = getSlingScriptHelper()
				.getService(PersonalizationConfigService.class);

		String component = get("componentName", String.class);
		log.info("Component name passed as parameter to provider is: " + component);
		log.info("Server Name: " + getRequest().getServerName());
		log.info("Remote Address: " + getRequest().getRemoteAddr());
		log.info("Remote Host: " + getRequest().getRemoteHost());
		/*
		 * String domain = new
		 * URL(getRequest().getRequestURL().toString()).getHost(); log.info(
		 * "Domain Name: " + domain);
		 */

		hostName = getRequest().getHeader("X-FORWARDED-FOR");
		if (StringUtils.isBlank(hostName)) {
			hostName = getRequest().getServerName();
		}
		log.info("Host Name resolved in provider is: " + hostName);

		String regionName = "";
		if (StringUtils.isNotBlank(hostName)) {
			String[] hostnames = hostName.split(Pattern.quote("."));
			log.info("Hostname length after split by dot is: " + hostnames.length);

			if (hostnames.length > 2) {
				regionName = hostnames[1];
			}
		}
		
		if(StringUtils.isBlank(regionName)){
			regionName = "calif";
		}
		
		log.info("Region name resolved in provider is: " + regionName);

		String contentBasePath = formatPath(configService.getContentRootPath());
		if (StringUtils.isNotBlank(regionName)) {
			Page regionPage = getPageManager().getPage(contentBasePath + regionName);
			if (regionPage != null) {
				regionDisplayName = regionPage.getTitle();
			}
		}
		log.info("Region display name resolved in provider is: " + regionDisplayName);

		if (StringUtils.isNotBlank(regionName) && StringUtils.isNotBlank(component)) {
			componentResource = getResourceResolver()
					.resolve(contentBasePath + regionName + JCR_CONTENT_PAR_PATH + component);
		}
		log.info("Component resource resolved in provider is: " + componentResource);

		log.info("End of Region Data Provider class");
	}

	private String formatPath(String contentRootPath) {
		if (!contentRootPath.endsWith("/")) {
			return contentRootPath.concat("/");
		} else {
			return contentRootPath;
		}
	}

	public String getHostName() {
		return hostName;
	}

	public String getRegionDisplayName() {
		return regionDisplayName;
	}

	public Resource getComponentResource() {
		return componentResource;
	}

}
