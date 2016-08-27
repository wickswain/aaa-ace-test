package com.aaa.ace.services.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.common.Constants;
import com.aaa.ace.services.PersonalizationConfigService;
import com.aaa.ace.services.RegionDataService;

/**
 * Personalization configuration service.
 *
 */
@Service({ PersonalizationConfigService.class })
@Component(immediate = true, metatype = true, label = "Personalization Configuration Service")
public class PersonalizationConfigServiceImpl implements PersonalizationConfigService {

    /**
     * Logger variable.
     */
    private static Logger logger = LoggerFactory.getLogger(PersonalizationConfigServiceImpl.class);

    /**
     * Content path configurable property.
     */
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "Personalization Content Root Path", description = "Enter the personalization content root node path", value = Constants.CONTENT_PERSONALIZATION_ACE_WWW_PATH)
    private static final String CONTENT_PATH = "contentPath";

    /**
     * Content root path string variable.
     */
    private String contentRootPath;

    @Reference
    private RegionDataService regionDataService;

    @Activate
    protected void activate(Map<String, Object> properties) {
        logger.info("[AEM PersonalizationConfigurationService]: activating configuration service");
        contentRootPath = PropertiesUtil.toString(properties.get(CONTENT_PATH),
                Constants.CONTENT_PERSONALIZATION_ACE_WWW_PATH);
    }

    @Override
    public String getContentRootPath() {
        return this.contentRootPath;
    }

    @Override
    public String getFormattedPersonalizationPagePath(SlingHttpServletRequest request,
            String pagePath) {

        if (StringUtils.isNotBlank(pagePath)
                && pagePath.contains(Constants.CONTENT_PERSONALIZATION_ACE_WWW_PATH)) {
            String[] pagePathNodes = pagePath.split(Constants.STRING_PATH_SEPARATOR);
            if (pagePathNodes.length > 4) {
                String regionPlaceholder = pagePathNodes[4];
                String regionName = regionDataService.getRegionInfo(request);

                pagePath = pagePath.replace(regionPlaceholder, regionName);
            }

        }

        return pagePath;
    }

}
