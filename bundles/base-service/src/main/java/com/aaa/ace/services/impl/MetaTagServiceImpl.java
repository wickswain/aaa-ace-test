/**
 * 
 */
package com.aaa.ace.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import com.aaa.ace.beans.PageMetaDataBean;
import com.aaa.ace.services.MetaTagService;
import com.day.cq.tagging.Tag;

/**
 * Meta Tag service implementation to get meta name based on tag. The logic used is as below.
 * The meta name is constructed with the tag namespace and tag path after replacing it with
 * underscore. This will enable us with unique name to minimize chances of partial match during
 * search filtering.
 * 
 * For example. meta tag with value west will be match "westways" and "west countries" both.
 * The meta content is Title of the CQ Tag passed.
 * @author yogesh.mahajan
 *
 */
@Component(immediate = true, metatype = false)
@Service(MetaTagService.class)
@Properties({ @Property(name = "service.vendor", value = "com.aaa.ace.services"),
    @Property(name = "service.description", value = "com.aaa.ace.services Meta Tag Service") })


public class MetaTagServiceImpl implements MetaTagService {

    public static final String COLON = ":";

    public static final String UNDERSCORE = "_";

    private static final String FORWARD_SLASH = "/";

    /*
     * Get meta data object based on tag name and title. 
     * @see com.aaa.ace.services.MetaTagService#getMetaData(com.day.cq.tagging.Tag)
     */
    @Override
    public PageMetaDataBean getMetaData(Tag tag) {
        PageMetaDataBean metaDataBean = new PageMetaDataBean();

        metaDataBean.setMetaName(getMetaName(tag.getTagID()));
        metaDataBean.setMetaContent(tag.getTitle());
        
        return metaDataBean;
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
    private String getMetaName(String tagID) {

        String tagName = tagID;

        // Check for null or blank
        if (StringUtils.isEmpty(tagName)) {
            return "";
        }

        if (StringUtils.contains(tagName, COLON)) {
            tagName = StringUtils.replace(tagName, COLON, UNDERSCORE);
        }

        if (StringUtils.contains(tagName, FORWARD_SLASH)) {
            tagName = StringUtils.replace(tagName, FORWARD_SLASH, UNDERSCORE);
        }
        return tagName.toLowerCase();
    }

}
