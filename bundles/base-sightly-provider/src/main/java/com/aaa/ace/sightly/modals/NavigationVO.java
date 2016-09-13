package com.aaa.ace.sightly.modals;

import java.util.List;

import com.day.cq.wcm.api.Page;

/**
 * Provides a POJO class that can be used to capture the navigation page
 * details.
 * 
 */
public class NavigationVO {

    private String uniqueId;
    
    private Page currentpage;
    
    private boolean hasChildren;
    
    private List<NavigationVO> navigList;

    /**
     * @return
     */
    public Page getCurrentpage() {
        return currentpage;
    }

    /**
     * @param currentpage
     */
    public void setCurrentpage(Page currentpage) {
        this.currentpage = currentpage;
    }

    /**
     * @return
     */
    public Boolean getHasChildren() {
        return hasChildren;
    }

    /**
     * @param hasChildren
     */
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
     * @return
     */
    public List<NavigationVO> getNavigList() {
        return navigList;
    }

    /**
     * @param navigList
     */
    public void setNavigList(List<NavigationVO> navigList) {
        this.navigList = navigList;
    }

    /**
     * @return
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uuid
     */
    public void setUniqueId(String uuid) {
        this.uniqueId = uuid;
    }

}
