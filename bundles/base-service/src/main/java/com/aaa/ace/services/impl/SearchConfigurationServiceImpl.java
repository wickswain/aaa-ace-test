/**
 * 
 */
package com.aaa.ace.services.impl;

import java.util.Dictionary;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.SearchConfigurationService;

/**
 * Search Configuration service to enable OSGI Configuration for API & Engine Id.
 * This will enable to use different API or search configuration for different AEM instance like
 * one for ACE and other for north region.
 * 
 * @author yogesh.mahajan
 *
 */

@Component(immediate = true, metatype = true, label = "AAA Google Search Configuration Service")
@Service(SearchConfigurationService.class)
@Properties({ @Property(name = "service.vendor", value = "com.aaa.ace.services"),
    @Property(name = "service.description", value = "com.aaa.ace.services Google Search Configuration Service"), })

public class SearchConfigurationServiceImpl implements SearchConfigurationService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Property(label = "Google Search API Key", description = "API key obtained from Google API Console.", unbounded = PropertyUnbounded.ARRAY)
    private static final String GOOGLE_API_KEY_PROPERTY_NAME = "google.api.key";

    @Property(label = "Google Search Engine Id", description = "Search Engine for Google Site Search configuration", unbounded = PropertyUnbounded.ARRAY)
    private static final String GOOGLE_SEARCH_ENGINE_ID_PROPERTY_NAME = "google.search.engine.id";
    
    private String apiKey;
    
    private String engineId;

    @Activate
    protected void activate(ComponentContext ctx) {
        
        logger.debug("Executing Search Configuration Service Activation");
        
        final Dictionary<?, ?> properties = ctx.getProperties();
        
        logger.debug("Properties: " + properties.toString());
        
        apiKey = PropertiesUtil.toString(properties.get(GOOGLE_API_KEY_PROPERTY_NAME), "");
        
        engineId = PropertiesUtil.toString(properties.get(GOOGLE_SEARCH_ENGINE_ID_PROPERTY_NAME), "");
        
        logger.debug("API key is {}, Search Engine Id is {} ", apiKey, engineId);
    }
    

    /**
     * 
     * @see com.aaa.ace.services.SearchConfigurationService#getGoogleAPIKey()
     */
    @Override
    public String getGoogleAPIKey() {
        return apiKey;
    }

    /* (non-Javadoc)
     * @see com.aaa.ace.services.SearchConfigurationService#getGoogleSearchEngine()
     */
    @Override
    public String getGoogleSearchEngine() {
        return engineId;
    }

}
