/**
 * 
 */
package com.aaa.ace.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
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
 * @author yogesh.mahajan
 *
 */

@Component(immediate = true, metatype = false, label = "Query Builder based content search service")
@Service(SearchService.class)

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

    /**
     * Allocating resolver and session objects to be used for query.
     * 
     * @param props - properties used for activation.
     * @throws LoginException 
     */
    @Activate
    public void activate(final Map<String, Object> props) throws LoginException {

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
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.aaa.ace.services.SearchService#getSearchResultJSON(java.util.Map)
     */
    @Override
    public String getSearchResultJSON(Map<String, String> searchInputMap) throws JSONException, RepositoryException {
        
        JSONObject returnJSON = new JSONObject();

      //Create a Query instance
        Query query = builder.createQuery(PredicateGroup.create(searchInputMap), session);
        
        //set start and number of results
        //query.setStart(0);
        //query.setHitsPerPage(10);
        
        //Get the query results
        SearchResult result = query.getResult();
        
                          
        // paging metadata
        int hitsPerPage = result.getHits().size(); 
        long totalMatches = result.getTotalMatches();
        long offset = result.getStartIndex();
        //long numberOfPages = totalMatches / 10;
        
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
            
            log.debug("Value Map: " + properties);
            
            JSONObject items = new JSONObject();
            items.put("path", path);
            items.put("link", pageResolver.resolveLinkURL(resourceResolver, hit.getResource().getParent().getPath()));
            //items.put("author", "2");
            //items.put("logo", "3");
            //returnJSON.("path", path);
            items.put("authorName", properties.get("authorName"));
            items.put("articleLogoImage", properties.get("articleLogoImage"));
            items.put("issueDate", properties.get("issueDate"));
            items.put("articleTitle", properties.get("articleTitle"));
            items.put("articleLogoAltText", properties.get("articleLogoAltText"));
            items.put("description", properties.get("jcr:description"));
            
            Resource articleHeroResource = hit.getResource().getChild("article-hero");
            if(null != articleHeroResource)
            {
                items.put("articleImage", articleHeroResource.getValueMap().get("articleImage"));
                items.put("articleImageAltText", articleHeroResource.getValueMap().get("alttext"));

            }
            
            //returnJSON.put("items", items);
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
