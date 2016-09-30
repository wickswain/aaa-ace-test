package com.aaa.ace.services;

import java.util.Map;


/**
 * Service interface to enable search based on query builder API 
 * 
 * @author yogesh.mahajan
 *
 */
public interface SearchService {
    
    
    /**
     * Get search results in form of JSON
     * @throws Exception 
     */
    
    String getSearchResultJSON(Map<String, String> searchInput) throws Exception;
    

}
