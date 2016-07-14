package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.ValueMap;

public class TitleSightlyProvider extends WCMUsePojo {

	private String jcrtitle;

	@Override
	public void activate() throws Exception {

		ValueMap properties = getProperties();

		jcrtitle=properties.get("jcr:title",String.class);
		//logic stuff to title
		jcrtitle+=" that is really awesome";

	}

	public String getJcrTitle(){
		return jcrtitle;
	}

}
