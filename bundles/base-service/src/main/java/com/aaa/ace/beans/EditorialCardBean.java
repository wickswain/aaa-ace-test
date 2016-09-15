package com.aaa.ace.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This is a simple POJO used to pass the article properties to slightly counterpart.
 * 
 * @author yogesh.mahajan
 *
 */
public class EditorialCardBean {

    private String title;

    private String description;

    private String imagePath;

    private String imageAltText;

    private String articlePath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageAltText() {
        return imageAltText;
    }

    public void setImageAltText(String imageAltText) {
        this.imageAltText = imageAltText;
    }

    public String getArticlePath() {
        return articlePath;
    }

    public void setArticlePath(String articlePath) {
        this.articlePath = articlePath;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
         
    }

}
