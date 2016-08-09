package com.aaa.ace.services;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Region data service is used to fetch the region information based on the
 * request.
 *
 */
public interface RegionDataService {
    String getRegionInfo(SlingHttpServletRequest request);
}
