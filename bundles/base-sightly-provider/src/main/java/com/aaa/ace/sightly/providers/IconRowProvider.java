package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * JAVA use API for Icon Row slightly component.
 * 
 * @author Vagner Polund
 *
 */
public class IconRowProvider extends WCMUsePojo {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private List<Resource> iconItems;
    private final String PROP_ICON_AMOUNT = "iconamount";


    @Override
    public void activate() throws Exception {
        iconItems = new ArrayList<>();
        int iconAmount=Integer.parseInt(this.getProperties().get(PROP_ICON_AMOUNT, String.class));
        Resource resource = this.getResource();
        ResourceResolver resourceResolver = this.getResourceResolver();
        Iterator<Resource> listChildren = this.getResource().listChildren();
        Map componentProperties = new HashMap<>();
        componentProperties.put("sling:resourceType", "/apps/ace-www/components/content/icon-row/icon-item");
        for (int i = 0; i < iconAmount; i++) {
            if(!listChildren.hasNext()){

                Resource create = resourceResolver.create(resource, "icon_item"+i+1, componentProperties);
                resourceResolver.commit();
                iconItems.add(create);
            }else{
                iconItems.add(listChildren.next());
            }

        }
        while(listChildren.hasNext()){
            resourceResolver.delete(listChildren.next());
            resourceResolver.commit();
        }
    }

    public List<Resource> getIcons() {
        return iconItems;
    }

}
