package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.ModifiableValueMap;
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
    private List<String> iconItems;
    private final String PROP_ICON_AMOUNT = "iconamount";
    private String classes;

    public String getClasses() {
        return classes;
    }

    @Override
    public void activate() throws Exception {
        iconItems = new ArrayList<>();
        int iconAmount=Integer.parseInt(this.getProperties().get(PROP_ICON_AMOUNT, String.class));
        // classes for icons
        classes= "";
        if(iconAmount == 3){
            classes = "col-md-4 col-sm-4 col-xs-12";
        }else if(iconAmount == 4){
            classes = "col-md-3 col-sm-3 col-xs-12";
        }

        for (int i = 0; i < iconAmount; i++) {
            iconItems.add("icon_item"+(i+1));
        }
    }

    public List<String> getIcons() {
        return iconItems;
    }

}