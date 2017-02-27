package com.aaa.ace.sightly.providers;

import java.text.NumberFormat;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to validate 'Actual Price', 'Discount Price' and 'Itemized Savings' properties, decimal values up to two digits in Membership Calculator component.
 *
 */

public class MembershipCalculatorProvider extends WCMUsePojo{
	
	private static final Logger log = LoggerFactory.getLogger(MembershipCalculatorProvider.class);

	private String actPrice;
	private String disPrice;
	private String itemSavings;
	
	@Override
	public void activate() throws Exception {
		
	 Resource resource = get("childResource",Resource.class);
	 actPrice = (String) resource.getValueMap().get("actualPrice");
	 disPrice = (String) resource.getValueMap().get("discountPrice");
	 itemSavings = (String) resource.getValueMap().get("itemizedSavings");
	 NumberFormat nf = NumberFormat.getInstance();
     Double tempActPrice = Double.parseDouble(actPrice);
     Double tempDisPrice = Double.parseDouble(disPrice);
     Double tempItemSavings = Double.parseDouble(itemSavings);
     nf.setMaximumFractionDigits(2);
     actPrice = nf.format(tempActPrice);
     disPrice = nf.format(tempDisPrice);
     itemSavings=nf.format(tempItemSavings);
	}
	
	public String getActPrice(){
		return actPrice;
	}
	
	public String getDisPrice(){
		return disPrice;
	}
	
	public String getItemSavings(){
		return itemSavings;
	}
}
