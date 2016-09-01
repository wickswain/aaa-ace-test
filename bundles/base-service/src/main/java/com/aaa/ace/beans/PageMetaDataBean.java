package com.aaa.ace.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class to represent a tag bean for adding meta to all page head.
 * 
 * @author yogesh.mahajan
 *
 */
public class PageMetaDataBean {

    /* Meta tag name. */
    String metaName;

    /* Meta Tag Content. */
    String metaContent;

    /**
     * @return the metaName
     */
    public String getMetaName() {
        return metaName;
    }

    /**
     * @param metaName the metaName to set
     */
    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    /**
     * @return the metaContent
     */
    public String getMetaContent() {
        return metaContent;
    }

    /**
     * @param metaContent the metaContent to set
     */
    public void setMetaContent(String metaContent) {
        this.metaContent = metaContent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
