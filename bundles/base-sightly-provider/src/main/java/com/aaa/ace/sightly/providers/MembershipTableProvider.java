package com.aaa.ace.sightly.providers;

import java.util.List;
import org.apache.sling.api.resource.Resource;
import com.aaa.ace.services.MultipleComponentProviderService;
import com.adobe.cq.sightly.WCMUsePojo;

public class MembershipTableProvider extends WCMUsePojo{
	
	private List<Resource> rowsList;
    private final String PROP_ROW_COUNT = "totalRowCount";
    private final String NODE_TYPE = "sling:resourceType";
    private final String ROW_NODE_PATH = "/apps/ace-www/components/content/membership-comparision-table/membership-table-row";
    private final String ROW_RESOURCE_NAME = "membership-table-row";
    
    private List<Resource> columnsList;
    private final String PROP_COLUMN_COUNT = "columnCount";
    private final String COLUMN_NODE_PATH = "/apps/ace-www/components/content/membership-comparision-table/membership-table-column";
    private final String COLUMN_RESOURCE_NAME = "membership-table-column";
    private int columnsCount = 0;
    
    @Override
    public void activate() throws Exception {
    	int rowCount=Integer.parseInt(this.getProperties().get(PROP_ROW_COUNT, String.class));
    	MultipleComponentProviderService compService = getSlingScriptHelper().getService(MultipleComponentProviderService.class);
    	rowsList = compService.createComponent(this.getResource(), rowCount, NODE_TYPE, ROW_NODE_PATH, ROW_RESOURCE_NAME);
    	
    	columnsCount=Integer.parseInt(this.getProperties().get(PROP_COLUMN_COUNT, "0"));
    	columnsList = compService.createComponent(this.getResource(), columnsCount, NODE_TYPE, COLUMN_NODE_PATH, COLUMN_RESOURCE_NAME);
    }

	public List<Resource> getRows() {
        return rowsList;
    }
	
	public List<Resource> getColumns() {
        return columnsList;
    }
	
	public int getColumnsCount() {
        return columnsCount;
    }
}
