package com.aaa.ace.sightly.providers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.beans.PageMetaDataBean;
import com.aaa.ace.services.MetaTagService;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * This is Java USE class for sightly templates, used to read the cq:tags and returns the list of
 * tag name and tag content using below logic.
 * Ideally used in search filters to put the tag data to match page meta information.

 * Meta Name: Tag ID after replacing colon(:) and slash(/) with underscore 
 * Meta Content: Tag Title
 * 
 * @author yogesh.mahajan
 *
 */

public class TagMetaProvider extends WCMUsePojo {

    String tagId;
    
    String metaName;
    
    String metaContent;

    /* Logger member */
    private final Logger log = LoggerFactory.getLogger(getClass());


    /**
     * On activation of the USE class, the tags on current page are mapped to meta name and content.
     */
    @Override
    public void activate() throws Exception {
        
        tagId = get("tagid" ,String.class);

        if (StringUtils.isEmpty(tagId)) {
            log.warn("Tag ID is black returning ");
            return;
        }

        log.debug("Tag Id {} ", tagId);

        /* get Tag object from tagid */
        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
        
        Tag tag = tagManager.resolve(tagId);
        
        /* Get meta service and meta data from tag */
        MetaTagService metaTagService = getSlingScriptHelper().getService(MetaTagService.class);
        
        if (null == metaTagService) {
            log.warn("Meta Tag Service is not available, returning");
            return;
        }

        PageMetaDataBean metaDataBean = metaTagService.getMetaData(tag);
        
        metaName = metaDataBean.getMetaName();
        metaContent = metaDataBean.getMetaContent();

        log.debug("Tag reolved to meta name {}, content resolves to {}", metaName, metaContent);
         
    }

    public String getMetaName() {
        return metaName;
    }

    public String getMetaContent() {
        return metaContent;
    }

}
