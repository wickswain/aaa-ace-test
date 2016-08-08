package com.aaa.ace.sightly.providers;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * Utility class to help a Sightly component to get a child node's properties of
 * current resource.
 */
public class ChildPropertyProvider extends WCMUsePojo {

    private Resource childResource;

    @Override
    public void activate() throws Exception {
        String childNode = get("child", String.class);
        childResource = getResource().getChild(childNode);
    }

    public Resource getChildResource() {
        return childResource;
    }

}
