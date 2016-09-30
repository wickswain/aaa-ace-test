package com.aaa.ace.sightly.providers;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.sightly.WCMUsePojo;

public class MembershipTableRowProvider extends WCMUsePojo{
	
    private int columnsCount = 0;
    private final String PROP_COLUMN_COUNT = "columnCount";
    
    @Override
    public void activate() throws Exception {
    	Resource parent = getResource().getParent();
    	columnsCount=Integer.parseInt(parent.getValueMap().get(PROP_COLUMN_COUNT, "0"));
    }

	public int getColumnsCount() {
        return columnsCount;
    }

}
