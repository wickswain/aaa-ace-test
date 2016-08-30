package com.aaa.ace.services;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Personalization configuration service.
 *
 * @author bharath.kambam
 *
 */
public interface PersonalizationConfigService {

    /**
     * Gets the personalization content root path to read the personalized
     * content.
     *
     * @return
     */
    String getContentRootPath();

    /**
     * Gets the valid URL by replacing the region name place holder with the
     * resolved region name through request.
     *
     * @return
     */
    String getFormattedPersonalizationPagePath(SlingHttpServletRequest request, String pagePath);

}
