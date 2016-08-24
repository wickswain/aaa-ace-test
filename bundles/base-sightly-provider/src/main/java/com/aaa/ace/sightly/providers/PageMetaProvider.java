package com.aaa.ace.sightly.providers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.slightly.beans.PageMetaTagBean;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;

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
    private List<PageMetaTagBean> metaTagList = new ArrayList<PageMetaTagBean>();

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

        Page currentPage = getCurrentPage();

        /*
         * Not sure if this is possible to have null current page but should be handled.
         */
        if (currentPage == null) {
            log.warn("Got current page as null returning empty list ");
            return;
        }

        Tag[] tags = currentPage.getTags();

        log.debug("Page {} has {} tags.", currentPage.getName(), tags.length);

        for (Tag tag : tags) {

            String metaName = getTagName(tag);

            String metaContent = tag.getTitle();

            log.info("Tag Name {}, Tag value {}", metaName, metaContent);
            metaTagList.add(new PageMetaTagBean(metaName, metaContent));
        }

        log.info("Created list {} ", metaTagList);

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
    private String getTagName(Tag tag) {

        String tagName = tag.getTagID();
        if (StringUtils.contains(tagName,COLON)) {
            tagName = StringUtils.replace(tagName,COLON, UNDERSCORE);
        }

        if (StringUtils.contains(tagName,FORWARD_SLASH)) {
            tagName = StringUtils.replace(tagName, FORWARD_SLASH, UNDERSCORE);
        }
        return tagName.toLowerCase();
    }

    /**
     * Returns Meta tag list.
     * 
     * @return
     */
    public List<PageMetaTagBean> getMetaList() {
        log.debug("getMetaList: Returning list {} ", metaTagList);
        return metaTagList;
    }

}
