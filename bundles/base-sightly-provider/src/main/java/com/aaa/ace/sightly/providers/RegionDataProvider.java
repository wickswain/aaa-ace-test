package com.aaa.ace.sightly.providers;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.PersonalizationConfigService;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;

/**
 * The Region Data Provider is useful to fetch the region information.
 * 
 * @author bharath.kambam
 *
 */
public class RegionDataProvider extends WCMUsePojo {

    private static final Logger log = LoggerFactory.getLogger(RegionDataProvider.class);
    private static final String JCR_CONTENT_PAR_PATH = "/jcr:content/content-par/";

    public String hostName;
    public String regionDisplayName;
    public Resource componentResource;

    @Override
    public void activate() throws Exception {
        log.debug("[AEM RegionDataProvider]: Start of Region Data Provider class");

        PersonalizationConfigService configService = getSlingScriptHelper()
                .getService(PersonalizationConfigService.class);

        String component = get("componentName", String.class);
        log.debug("Component name passed as a parameter to provider is: " + component);

        hostName = new URL(getRequest().getRequestURL().toString()).getHost();
        log.debug("Host Name resolved in provider is: " + hostName);

        // region resolved by default to California.
        String regionName = "";
        if (hostName.split(".").length > 2) {
            regionName = hostName.split(".")[1];
        } else {
            regionName = "calif";
        }
        log.debug("Region name resolved in provider is: " + regionName);

        String contentBasePath = formatPath(configService.getContentRootPath());
        if (StringUtils.isNotBlank(regionName)) {
            Page regionPage = getPageManager().getPage(contentBasePath + regionName);
            if (regionPage != null) {
                regionDisplayName = regionPage.getTitle();
            }
        }
        log.debug("Region display name resolved in provider is: " + regionDisplayName);

        if (StringUtils.isNotBlank(regionName) && StringUtils.isNotBlank(component)) {
            componentResource = getResourceResolver()
                    .resolve(contentBasePath + regionName + JCR_CONTENT_PAR_PATH + component);
        }
        log.debug("Component resource resolved in provider is: " + componentResource);

        log.debug("End of Region Data Provider class");
    }

    /*
     * Format and returns the actual path.
     *
     * @param contentRootPath
     * 
     * @return formated path
     */
    private String formatPath(String contentRootPath) {
        if (!contentRootPath.endsWith("/")) {
            return contentRootPath.concat("/");
        } else {
            return contentRootPath;
        }
    }

    /**
     * gets the host name of the client.
     *
     * @return
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * gets the region display name resolved from the request.
     *
     * @return
     */
    public String getRegionDisplayName() {
        return regionDisplayName;
    }

    /**
     * gets the personalization component resource based on the component name
     * received in the provider.
     *
     * @return
     */
    public Resource getComponentResource() {
        return componentResource;
    }

}
