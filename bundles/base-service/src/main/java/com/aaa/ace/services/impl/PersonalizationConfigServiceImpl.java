package com.aaa.ace.services.impl;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.PersonalizationConfigService;

/**
 * Personalization config service.
 *
 */
@Service(PersonalizationConfigService.class)
@Component(immediate = true, metatype = true, label = "Personalization Configuration Service")
public class PersonalizationConfigServiceImpl implements PersonalizationConfigService {

	private static Logger logger = LoggerFactory.getLogger(PersonalizationConfigServiceImpl.class);
	
	@Property(unbounded = PropertyUnbounded.DEFAULT, label = "Personalization Content Root Path", 
			description = "Enter the personalization content root node path",
			value = "/content/personalization/ace-www/")
	private static final String CONTENT_PATH = "contentPath";
	
	private String contentRootPath;
	
	@Activate
	protected void activate(Map<String, Object> properties) {
		logger.info("[AEM PersonalizationConfigurationService]: activating configuration service");
		logger.info("Properties from config manager: " + properties.toString());
		contentRootPath = PropertiesUtil.toString(properties.get(CONTENT_PATH), "/content/personalization/ace-www/");
	}

	public String getContentRootPath() {
		return this.contentRootPath;
	}

}
