/**
 * 
 */
package com.aaa.ace.sightly.providers;

import com.aaa.ace.services.SearchConfigurationService;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * @author yogesh.mahajan
 *
 */
public class SearchConfigProvider extends WCMUsePojo {

    String googleApiKey;
    
    String googleSearchEngineId;
    
    
    /* (non-Javadoc)
     * @see com.adobe.cq.sightly.WCMUsePojo#activate()
     */
    @Override
    public void activate() throws Exception {

        SearchConfigurationService searchConfigurationService = 
                getSlingScriptHelper().getService(SearchConfigurationService.class);

        
        googleApiKey = searchConfigurationService.getGoogleAPIKey();
        
        googleSearchEngineId = searchConfigurationService.getGoogleSearchEngine();

    }


    /**
     * @return the googleApiKey
     */
    public String getGoogleApiKey() {
        return googleApiKey;
    }


    /**
     * @return the googleSearchEngineId
     */
    public String getGoogleSearchEngineId() {
        return googleSearchEngineId;
    }
    
    

}
