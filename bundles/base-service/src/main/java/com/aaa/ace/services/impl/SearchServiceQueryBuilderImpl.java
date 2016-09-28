/**
 * 
 */
package com.aaa.ace.services.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.PageSuffixResolverService;
import com.aaa.ace.services.SearchService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

/**
 * Service to execute QueryBuilder API and return the JSON response.
 * 
 * @author yogesh.mahajan
 *
 */

@Component(immediate = true, metatype = true, label = "AAA Article List Service")
@Service(SearchService.class)
@Properties({ 
        @Property(name = "service.vendor", value = "com.aaa.ace.services"),
        @Property(name = "service.description", value = "com.aaa.ace.services Article List Search Service")
    })

public class SearchServiceQueryBuilderImpl implements SearchService {
    
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private QueryBuilder builder;
    
    @Reference
    PageSuffixResolverService pageResolver;
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    ResourceResolver resourceResolver = null;

    Session session = null;
    
    private static final String DEFAULT_ARTICLE_SEARCH_PATH = "/content/ace-www";
    
    private static final String DEFAULT_ARTICLE_TEMPLATE = "/apps/ace-www/templates/article";
    
    @Property(label = "Article Search Path", description = "The path on which articles should be search. Should be as specific possible for efficient query.", 
                    unbounded = PropertyUnbounded.DEFAULT, value = DEFAULT_ARTICLE_SEARCH_PATH)
    private static final String ARTICLE_SEARCH_PATH_NAME = "articles.search.path";
    

    @Property(label = "Article Template", description = "The AEM template used to create article pages.", 
            unbounded = PropertyUnbounded.DEFAULT, value = DEFAULT_ARTICLE_TEMPLATE)
    private static final String ARTICLE_TEMPLATE_NAME = "articles.search.template";

    
    //path on which articles would be searched
    String path;
    
    //template for articles
    String template;

    /**
     * Allocating resolver and session objects to be used for query.
     * 
     * @param props - properties used for activation.
     * @throws LoginException 
     */
    @Activate
    protected void activate(ComponentContext ctx) throws LoginException {

        try {
            
            /* Get resource resolver with system user, making service name for mapper generic
             * so that it can be used at other components as well. 
             */
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, "jcrqueryservice");
            resourceResolver = resolverFactory.getServiceResourceResolver(paramMap);

        } catch (LoginException e) {
            log.error("Exception in getting resouce resolver", e);
        }

        if (null != resolverFactory)
        {
            session = resourceResolver.adaptTo(Session.class);
            log.debug("Successfully aquired session");
        }
        else
        {
            log.warn("Resource Resolver is null, couldn't aquire session.");
        }
        
        log.debug("Reading OSGI property to get path");
        
        final Dictionary<?, ?> properties = ctx.getProperties();
        
        path = PropertiesUtil.toString(properties.get(ARTICLE_SEARCH_PATH_NAME), DEFAULT_ARTICLE_SEARCH_PATH);
        
        template = PropertiesUtil.toString(properties.get(ARTICLE_TEMPLATE_NAME), DEFAULT_ARTICLE_TEMPLATE);
        
        log.debug("Search Path configured to {}, template is {} ", path, template);
    }
    
    /**
     * Method to invoke query API and return the JSON response.
     * 
     */
    @Override
    public String getSearchResultJSON(Map<String, String> searchInputMap) throws JSONException, RepositoryException {
        
        //our final answer
        JSONObject returnJSON = new JSONObject();
        
        //add configured path to query
        searchInputMap.put("path", path);
        
        //bind article template
        searchInputMap.put("1_property", "cq:template");
        searchInputMap.put("1_property.value", template);
        searchInputMap.put("1_property.operation", "equal");

        
        //Create a Query instance
        Query query = builder.createQuery(PredicateGroup.create(searchInputMap), session);
        
        //Get the query results
        SearchResult result = query.getResult();
        
        // paging metadata
        int hitsPerPage = result.getHits().size(); 
        long totalMatches = result.getTotalMatches();
        
        //UI is expecting 1 based index but query API is 0 based.
        long offset = result.getStartIndex() + 1;
        
        JSONObject searchInfo = new JSONObject();
        searchInfo.put("totalResults", totalMatches);
        searchInfo.put("startIndex", offset);
        searchInfo.put("noOfResult", hitsPerPage);
        
        returnJSON.accumulate("searchInformation", searchInfo);
        
        // iterating over the results
        log.debug("Number of results: " + result.getHits().size());
        for (Hit hit : result.getHits()) {
                        
            String path = hit.getPath();
            ValueMap properties = hit.getProperties();
            
            log.debug("Properties: " + properties);
            
            //Create items JSON object 
            JSONObject items = new JSONObject();
            
            items.put("articleTitle", properties.get("articleTitle", String.class));
            
            items.put("path", path);
            items.put("link", pageResolver.resolveLinkURL(resourceResolver, 
                                            hit.getResource().getParent().getPath()));

            items.put("authorName", properties.get("authorName"));
            items.put("articleLogoImage", properties.get("articleLogoImage"));
            items.put("issueDate", properties.get("issueDate"));
            items.put("articleLogoAltText", properties.get("articleLogoAltText"));
            items.put("description", properties.get("text"));
            
            Resource articleHeroResource = hit.getResource().getChild("article-hero");
            if(null != articleHeroResource)
            {
                items.put("articleImage", articleHeroResource.getValueMap().get("articleImage"));
                items.put("articleImageAltText", articleHeroResource.getValueMap().get("alttext"));

            }
            
            //add items to outer JSON
            returnJSON.accumulate("items", items);
        }
        
        return returnJSON.toString();
    }
    
    @Deactivate
    public void deactivate()
    {
        if(null != session)
        {
            session.logout();
        }
        
        if(null != resourceResolver)
        {
            resourceResolver.close();
        }
    }
}
