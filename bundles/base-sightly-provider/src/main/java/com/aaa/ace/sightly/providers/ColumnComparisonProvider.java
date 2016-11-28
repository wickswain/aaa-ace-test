package com.aaa.ace.sightly.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * Column Comparison provider
 *
 * @author Rudy Elizarraras
 *
 */

public class ColumnComparisonProvider extends WCMUsePojo {

    private List<Resource> columnItems;
    private final String COLUMN_COUNT = "columnCount";

    @Override
    public void activate() throws Exception {

        columnItems = new ArrayList<>();
        Map componentProperties = new HashMap<>();
        int columnAmount = Integer.parseInt(this.getProperties().get(COLUMN_COUNT, String.class));
        Resource resource = this.getResource();
        ResourceResolver resourceResolver = this.getResourceResolver();
        Iterator<Resource> columnChildren = this.getResource().listChildren();
        componentProperties.put("sling:resourceType",
                "/apps/aaa-core/components/content/column-comparison/column-item");

        for (int i = 0; i < columnAmount; i++) {
            if (!columnChildren.hasNext()) {
                Resource create = resourceResolver.create(resource, "column_item" + (i + 1),
                        componentProperties);
                resourceResolver.commit();
                columnItems.add(create);
            } else {
                columnItems.add(columnChildren.next());
            }

        }

        while (columnChildren.hasNext()) {
            resourceResolver.delete(columnChildren.next());
            resourceResolver.commit();
        }
    }

    public List<Resource> getColumns() {
        return columnItems;
    }
}
