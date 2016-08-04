package com.aaa.ace.services.impl;

/**
 * Created by vpolund on 8/4/16.
 */

import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

import java.util.Dictionary;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.ExampleService;

@Component(immediate = true, metatype = true, label = "aaa-ace Example Service")
@Service
@Properties({
        @Property(name = "service.vendor", value = "com.aaa"),
        @Property(name = "service.description", value = "com.axis41 Example OSGI service."),
})
public class ExampleServiceImpl implements ExampleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /* example property */
    @Property(label = "Example Names Property", description = "property description goes here", unbounded = PropertyUnbounded.ARRAY)
    private static final String DEFAULT_NAMES = "prop.names";
    private String[] names;

    @Override
    public String[] getNames() {

        //query jcr for some content associated with these name
        return names;
    }

    @Override
    public void setNames(String[] names) {
        this.names = names;
    }

    @Activate
    protected void activate(ComponentContext ctx) {
        final Dictionary<?, ?> properties = ctx.getProperties();
        names = PropertiesUtil.toStringArray(properties.get(DEFAULT_NAMES), new String[0]);
    }
}
