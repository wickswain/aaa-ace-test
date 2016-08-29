package com.aaa.ace.sightly.providers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

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

public class TagMetaProvider extends WCMUsePojo {

    String tagId;
    
    String metaName;
    
    String metaContent;

    /* Logger member */
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String COLON = ":";

    public static final String UNDERSCORE = "_";

    private static final String FORWARD_SLASH = "/";

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

        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
        
        Tag tag = tagManager.resolve(tagId);
        
         metaName = getTagName(tag);

         metaContent = tag.getTitle();

         log.info("Tag reolved to meta name {}, content resolves to {}", metaName, metaContent);
        }


    /**
     * The tag name is constructed with the namespace and tag path after replacing it with
     * underscore. This will enable us with unique name to minimize chances of partial match during
     * search filtering.
     * 
     * For example. meta tag with value west will be match "westways" and "west countries" both.
     * 
     * @param tag
     * @return
     */
    public String getTagName(Tag tag) {

        String tagName = tag.getTagID();
        if (StringUtils.contains(tagName,COLON)) {
            tagName = StringUtils.replace(tagName,COLON, UNDERSCORE);
        }

        if (StringUtils.contains(tagName,FORWARD_SLASH)) {
            tagName = StringUtils.replace(tagName, FORWARD_SLASH, UNDERSCORE);
        }
        return tagName.toLowerCase();
    }

    public String getMetaName() {
        return metaName;
    }

    public String getMetaContent() {
        return metaContent;
    }

}
