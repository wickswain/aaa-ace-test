package com.aaa.ace.sightly.providers;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.sightly.WCMUse;

/**
 * This sightly provider is used to get the child resource properties of current
 * resource.
 *
 */
@SuppressWarnings("deprecation")
public class ChildResourceProvider extends WCMUse {

    /**
     * child resource variable.
     */
    private Resource childResource;

    @Override
    public void activate() throws Exception {
        String childNode = get("child", String.class);
        childResource = getResource().getChild(childNode);
    }

    /**
     * gets the child resource matching with the name passed as parameter.
     *
     * @return
     */
    public Resource getChildResource() {
        return childResource;
    }

}
