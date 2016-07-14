package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.ValueMap;

public class TitleSightlyProvider extends WCMUsePojo {

	private String jcrtitle;
	private String[] titleList;

	@Override
	public void activate() throws Exception {

		ValueMap properties = getProperties();

		jcrtitle=properties.get("jcr:title",String.class);
		//logic stuff to title
		jcrtitle+=" that is really awesome";

		titleList = properties.get("titlelistItems",String[].class);
		//logic stuff to title list
		for(int i=0;i<titleList.length;i++){
			titleList[i]=titleList[i]+" some cool string";
		}

	}

	public String getJcrTitle(){
		return jcrtitle;
	}
	public String[] getTitleList(){
		return titleList; }

}
