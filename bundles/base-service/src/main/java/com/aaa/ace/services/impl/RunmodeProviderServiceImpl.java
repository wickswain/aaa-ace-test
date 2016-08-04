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
@Properties({
        @Property(name = "service.vendor", value = "com.aaa.ace"),
        @Property(name = "service.description", value = "com.aaa.ace Runmode Provider OSGI service."), })
public class RunmodeProviderServiceImpl implements RunmodeProviderService{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Property(label = "Environment Property", 
            description = "Domain name (Environment) Property for the external app URL",
            unbounded = PropertyUnbounded.ARRAY)
    private static final String DEFAULT_ENV = "env";
    
    private String env;

    @Activate
    protected void activate(ComponentContext ctx) {
        logger.info("Enter into RunmodeProviderServiceImpl activate method");
        final Dictionary<?, ?> properties = ctx.getProperties();
        logger.info("Properties: " + properties.toString());
        env = PropertiesUtil.toString(properties.get(DEFAULT_ENV), "uat");
        logger.info("Env Value from Osgi Config: " + env);
    }

	/**
	 * @return the env
	 */
	public String getEnv() {
		return env;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(String env) {
		this.env = env;
	}

}
