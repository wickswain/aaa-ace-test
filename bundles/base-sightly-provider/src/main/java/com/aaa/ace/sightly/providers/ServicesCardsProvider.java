package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by E644282 on 09/13/2016.
 */
public class ServicesCardsProvider extends WCMUsePojo{

    public static final int CARD_ROW_AMOUNT = 3;
    private int requestedRowsAmt;

    private List<String> servicesCardPaths;

    public void activate ( ) throws Exception{

        servicesCardPaths = new ArrayList();

        requestedRowsAmt = Integer.parseInt(this.getProperties().get("rowamount", String.class));

        //loop over the row amount times the card row amount. in this case it row * 3
            //make unique path for each iteration. in this case it's servicecardpath+i
            //add unique string to servicecardPaths

        for (int i = 0; i<(requestedRowsAmt* CARD_ROW_AMOUNT); i++){
            String addPathName="servicecard"+(i+1);
            servicesCardPaths.add(addPathName);
        }

    }
    public List<String> getCardPaths(){
        return servicesCardPaths;
    }
}
