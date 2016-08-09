package com.aaa.ace.services.impl;

import java.util.Dictionary;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.RunmodeProviderService;

@Component(immediate = true, metatype = true, label = "axis41-core Example Service")
@Service
@Properties({ @Property(name = "service.vendor", value = "com.aaa.ace"),
        @Property(name = "service.description", value = "com.aaa.ace Runmode Provider OSGI service."), })
public class RunmodeProviderServiceImpl implements RunmodeProviderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Property(label = "Environment Property", description = "Domain name (Environment) Property for the external app URL", unbounded = PropertyUnbounded.ARRAY)
    private static final String ENVIRONMENT_PROPERTY_NAME = "environment";

    private String runmode;

    @Activate
    protected void activate(ComponentContext ctx) {
        logger.info("Enter into RunmodeProviderServiceImpl activate method");
        final Dictionary<?, ?> properties = ctx.getProperties();
        logger.info("Properties: " + properties.toString());
        runmode = PropertiesUtil.toString(properties.get(ENVIRONMENT_PROPERTY_NAME), "dev");
        logger.info("Env Value from Osgi Config: " + runmode);
    }

    /**
     * @return the env
     */
    public String getEnvironmentInfo() {
        return runmode;
    }

}
