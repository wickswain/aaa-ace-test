package com.aaa.ace.sightly.providers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.RegionDataService;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This provider uses to get the favicon path based on the region.
 *
 */
public class FavIconProvider extends WCMUsePojo {

    private static final Logger log = LoggerFactory.getLogger(FavIconProvider.class);

    /**
     * favicon path variable.
     */
    private String path;

    @Override
    public void activate() throws Exception {
        String designPath = getCurrentDesign().getPath();
        RegionDataService regionService = getSlingScriptHelper()
                .getService(RegionDataService.class);
        String regionName = regionService.getRegionInfo(getRequest());

        if (StringUtils.isNotBlank(designPath) && StringUtils.isNotBlank(regionName)) {
            if (regionName.equalsIgnoreCase("calif")) {
                path = designPath + "/favicon-calif.ico";
            } else {
                path = designPath + "/favicon-default.ico";
            }
        } else {
            log.debug("No designs found.");
        }

    }

    /**
     * gets the child resource matching with the name passed as parameter.
     *
     * @return Resource
     */
    public String getPath() {
        return path;
    }

}
