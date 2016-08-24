package com.aaa.ace.slightly.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class to represent a tag bean for adding meta to all page head.
 * 
 * @author yogesh.mahajan
 *
 */
public class PageMetaTagBean {

    /* Meta tag name. */
    String metaTagName;

    /* Meta Tag Content. */
    String metaTagContent;
    
    /**
     * Constructor to create bean for give tag.
     * 
     * @param tagName
     * @param tagValue
     */
    public PageMetaTagBean(String tagName, String tagValue) {

        this.metaTagName = tagName;
        this.metaTagContent = tagValue;
    }

    public String getMetaTagName() {
        return metaTagName;
    }

    public void setMetaTagName(String metaTagName) {
        this.metaTagName = metaTagName;
    }

    public String getMetaTagContent() {
        return metaTagContent;
    }

    public void setMetaTagContent(String metaTagContent) {
        this.metaTagContent = metaTagContent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
