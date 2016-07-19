package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TitleSightlyProvider extends WCMUsePojo {

	public static final String JCR_TITLE = "jcr:title";
	public static final String ROOTPARENT_PROP = "rootparent";
	private String jcrtitle;
	private String[] titleList;
	private List<String> childTitles;

	@Override
	public void activate() throws Exception {

		ValueMap properties = getProperties();

		jcrtitle=properties.get(JCR_TITLE,String.class);
		//logic stuff to title
		jcrtitle+=" that is really awesome";

		titleList = properties.get("titlelistItems",String[].class);
		//logic stuff to title list
		for(int i=0;i<titleList.length;i++){
			titleList[i]=titleList[i]+" some cool string";
		}

		childTitles= new ArrayList<>();

		String rootParentPath = properties.get(ROOTPARENT_PROP, String.class);


		Resource rootPageResource = this.getResourceResolver().getResource(rootParentPath);
		Page rootPage = rootPageResource.adaptTo(Page.class);


		Iterator<Page> childPageIterator = rootPage.listChildren();

		while(childPageIterator.hasNext()){
			Page currPage=childPageIterator.next();
			//ValueMap valueMap = currPage.getContentResource().getValueMap();
			childTitles.add(currPage.getTitle());
		}


	}

	public String getJcrTitle(){
		return jcrtitle;
	}
	public String[] getTitleList(){
		return titleList; }

	public List<String> getChildTitles() {
		return childTitles;
	}
}
