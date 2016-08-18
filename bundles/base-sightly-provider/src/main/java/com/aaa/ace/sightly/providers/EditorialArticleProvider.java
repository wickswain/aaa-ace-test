package com.aaa.ace.sightly.providers;

import com.aaa.ace.beans.EditorialCardBean;
import com.aaa.ace.services.EditorialArticleService;
import com.adobe.cq.sightly.WCMUsePojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * JAVA use API for Editorial Article slightly component.
 * 
 * @author yogesh.mahajan
 *
 */
public class EditorialArticleProvider extends WCMUsePojo {

    private final Logger log = LoggerFactory.getLogger(getClass());

    List<EditorialCardBean> cards;

    /**
     * Activate the USE class by calling article service and get the properties in bean list.
     */
    @Override
    public void activate() throws Exception {

        log.debug("Started Activate of EditorialArticleProvider");

        // Get all parameters configured by author
        String path = getProperties().get("searchPath", String.class);

        String[] tagArray = getProperties().get("tags", String[].class);

        List<String> searchTagList = (null != tagArray) ? Arrays.asList(tagArray) : null;

        boolean usePopularity = Boolean
                .valueOf(getProperties().get("popularityFlag", String.class));
        
        log.debug("Read properties - Path: {}, Tags: {}, UsePopularity: {} ", 
                                            path, searchTagList, usePopularity);

        // Get the article service
        EditorialArticleService articleService = getSlingScriptHelper().getService(
                EditorialArticleService.class);

        // call the article service
        cards = articleService.getEditorialArticles(path, searchTagList, usePopularity);

        if (cards.size() == 0) {
            log.warn("===> No Articles found ");
        }

        log.debug("End Activate of EditorialArticleProvider");
    }

    public List<EditorialCardBean> getCards() {
        return cards;
    }

}
