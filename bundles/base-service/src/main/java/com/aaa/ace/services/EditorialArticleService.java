package com.aaa.ace.services;

import com.aaa.ace.beans.EditorialCardBean;

import java.util.List;

/**
 * Service interface for Editorial Card service. 
 * This implementation should encapsulate the logic to fetch articles.
 * 
 * @author yogesh.mahajan
 *
 */
public interface EditorialArticleService {

    /**
     * Get editorial articles based on criteria selected by author.
     * 
     * @param path
     * @param searchTagList
     * @param usePopularity
     * @return
     */
    List<EditorialCardBean> getEditorialArticles(String path, List<String> searchTagList,
            boolean usePopularity);

}
