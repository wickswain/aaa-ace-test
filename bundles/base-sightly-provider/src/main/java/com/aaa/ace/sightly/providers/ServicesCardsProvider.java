package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.*;

/**
 * Created by E644282 on 09/13/2016.
 */
public class ServicesCardsProvider extends WCMUsePojo{

    private int requestedRowsAmt;

    private List<Resource> servicesCards;

    public void activate ( ) throws Exception{

        servicesCards = new ArrayList<>();

        Map servicesCardsProperties = new HashMap<>();

        requestedRowsAmt = Integer.parseInt(this.getProperties().get("rowamount", String.class));

        Resource resource = this.getResource();

        ResourceResolver resourceResolver = this.getResourceResolver();

        Iterator<Resource> serviceCardsChildren = this.getResource().listChildren();

        servicesCardsProperties.put("sling:resourceType", "/apps/ace-www/components/content/services-cards/service-card");


        for (int i=0; i<requestedRowsAmt; i++){
            if (!serviceCardsChildren.hasNext()){
                Resource create = resourceResolver.create(resource, "service_card" + (i+1), servicesCardsProperties);
                resourceResolver.commit();
                servicesCards.add(create);
            }else{
                servicesCards.add(serviceCardsChildren.next());
            }
        }

        while(serviceCardsChildren.hasNext()){
            resourceResolver.delete(serviceCardsChildren.next());
            resourceResolver.commit();
        }
    }
    public List<Resource> getServiceCards(){
        return servicesCards;
    }
}
