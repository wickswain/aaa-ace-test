package com.aaa.ace.sightly.providers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.beans.PageMetaDataBean;
import com.aaa.ace.services.MetaTagService;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;

/**
 * This is Java USE class for slightly templates, used to read the cq:tags and returns the list of
 * tag name and tag content using below logic.
 * 
 * Meta Name: Tag ID after replacing colon(:) and slash(/) with underscore 
 * Meta Content: Tag Title
 * 
 * @author yogesh.mahajan
 *
 */

public class PageMetaProvider extends WCMUsePojo {

    /* List to be returned */
    private List<PageMetaDataBean> metaTagList = new ArrayList<PageMetaDataBean>();

    /* Logger member */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * On activation of the USE class, the tags on current page are mapped to meta name and content.
     */
    @Override
    public void activate() throws Exception {

        MetaTagService metaTagService = getSlingScriptHelper().getService(MetaTagService.class);
        
        if (null == metaTagService) {
            log.warn("Meta Tag Service is not available, returning");
            return;
        }
        
        for (Tag tag : getCurrentPage().getTags()) {
            log.debug("Getting meta data for tag {}", tag);
            metaTagList.add(metaTagService.getMetaData(tag));
        }

        log.debug("Page {} has meta data list: {} ", getCurrentPage().getName(), metaTagList);

    }


    /**
     * Returns Meta tag list.
     * 
     * @return
     */
    public List<PageMetaDataBean> getMetaList() {
        
        log.debug("getMetaList: Returning list {} ", metaTagList);
        return metaTagList;
        
    }

}
