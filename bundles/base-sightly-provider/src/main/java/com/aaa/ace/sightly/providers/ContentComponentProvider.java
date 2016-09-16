package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.*;

/**
 * Two Column Comparison provider
 *
 * @author Rudy Elizarraras
 *
 */


public class ContentComponentProvider extends WCMUsePojo{

    private List<Resource> elementItems;     ////base variable for collection of column pairs
    private final String ELEMENT_COUNT = "elementcount";  ///config column count property name
    private final String CONTENT_PATH = "/apps/ace-www/components/content/";

    private UUID uniqueID;

    @Override
    public void activate() throws Exception{

        //String myArg = this.get("myArg", String.class);
        String contentItemPath = this.get("itemPath", String.class);

        ////collection for requested items
        elementItems = new ArrayList<>();
        Map componentProperties = new HashMap<>();
        ////get amount of requested items
        int elementAmount = Integer.parseInt(this.getProperties().get(ELEMENT_COUNT, String.class));
        Resource resource = this.getResource();
        ResourceResolver resourceResolver = this.getResourceResolver();
        Iterator<Resource> elementChildren = this.getResource().listChildren();
       componentProperties.put("sling:resourceType", CONTENT_PATH + contentItemPath);

        ////loop through requested elements
        for (int i = 0; i < elementAmount; i++) {
            if(!elementChildren.hasNext()){
                uniqueID = UUID.randomUUID();
                ////create element item
                Resource create = resourceResolver.create(resource, "element_item"+(uniqueID), componentProperties);
                resourceResolver.commit();
                ///add to array
                elementItems.add(create);
            }else{
                elementItems.add(elementChildren.next());
            }
        }

        ///cleanup
        while(elementChildren.hasNext()){
            resourceResolver.delete(elementChildren.next());
            resourceResolver.commit();
        }
    }


    public List<Resource> getItems() {
        return elementItems;
    }

}


