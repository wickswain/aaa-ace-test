package com.aaa.ace.services;

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

}
