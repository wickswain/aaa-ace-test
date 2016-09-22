/**
 * 
 */
package com.aaa.ace.services;


/**
 * Interface for search configuration like API key and Search Engine IDs
 * 
 * @author yogesh.mahajan
 *
 */
public interface SearchConfigurationService {
    
    /*
     * Get Google API Key.
     */
    String getGoogleAPIKey();
    
    /*
     * Returns Google Search Engine.
     */
    String getGoogleSearchEngine();
    

}
