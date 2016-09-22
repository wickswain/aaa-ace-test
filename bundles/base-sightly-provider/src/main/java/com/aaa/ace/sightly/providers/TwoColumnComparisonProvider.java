package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Two Column Comparison provider
 *
 * @author Rudy Elizarraras
 *
 */


public class TwoColumnComparisonProvider extends WCMUsePojo{

    private List<Resource> columnItems;     ////base variable for collection of column pairs
    private final String COLUMN_COUNT = "columnCount";  ///config column count property name
    private final String CONTENT_PATH = "/apps/ace-www/components/content/";

    @Override
    public void activate() throws Exception{

//        String myArg = this.get("myArg", String.class);

        ////collection for requested column pairs
        columnItems = new ArrayList<>();
        Map componentProperties = new HashMap<>();
        ////get amount of column pairs
        int columnAmount = Integer.parseInt(this.getProperties().get(COLUMN_COUNT, String.class));
        Resource resource = this.getResource();
        ResourceResolver resourceResolver = this.getResourceResolver();
        Iterator<Resource> columnChildren = this.getResource().listChildren();
//       componentProperties.put("sling:resourceType", CONTENT_PATH);
       componentProperties.put("sling:resourceType", "/apps/ace-www/components/content/two-column-comparison/two-column-item");

        ////loop through requested column pairs
        for (int i = 0; i < columnAmount; i++) {
            if(!columnChildren.hasNext()){
                ////create column item
                Resource create = resourceResolver.create(resource, "column_item"+(i+1), componentProperties);
                resourceResolver.commit();
                ///add to array
                columnItems.add(create);
            }else{
                columnItems.add(columnChildren.next());
            }

        }

        ///cleanup
        while(columnChildren.hasNext()){
            resourceResolver.delete(columnChildren.next());
            resourceResolver.commit();
        }
    }


    public List<Resource> getColumns() {
        return columnItems;
    }


}


