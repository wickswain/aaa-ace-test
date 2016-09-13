package com.aaa.ace.sightly.providers;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.sightly.WCMUsePojo;



/**
 * This sightly provider is used to get the child resource properties of current resource.
 *
 */
public class ChildResourceProvider extends WCMUsePojo {

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
     * @return Resource
     */
    public Resource getChildResource() {
        return childResource;
    }

    /**
     * Get number of children.
     */
    public Integer getChildLength() {
        int length = 0;

        // Could not find any API that returns number of children so have to loop through and count.
        Iterator<Resource> resourceIterator = childResource.listChildren();

        while (resourceIterator.hasNext()) {
            resourceIterator.next();
            length++;
        }
        
        
        return length;

    }

}
