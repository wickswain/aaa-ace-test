package com.aaa.ace.sightly.providers;

import com.aaa.ace.services.ExampleService;
import com.adobe.cq.sightly.WCMUsePojo;


/**
 * Created by vpolund on 8/5/16.
 */
public class SimpleComponentProvider extends WCMUsePojo {

    private String[] names;

    @Override
    public void activate() throws Exception {

        ExampleService es = getSlingScriptHelper().getService(ExampleService.class);


        names = es.getNames();



    }

    public String[] getNames() {
        return names;
    }
}