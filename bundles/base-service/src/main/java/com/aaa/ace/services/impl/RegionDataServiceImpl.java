package com.aaa.ace.services.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.RegionDataService;

/**
 * This is an impl class to fetch region data
 *
 */
/**
 * @author manish.singh
 *
 */
@Component
@Service({ RegionDataService.class })
@Properties({ @Property(name = "service.vendor", value = "com.aaa.ace.services"),
        @Property(name = "service.description", value = "com.aaa.ace.services Region Data Service"), })
public class RegionDataServiceImpl implements RegionDataService {

    private static final Logger log = LoggerFactory.getLogger(RegionDataServiceImpl.class);

    public String regionName;

    @Override
    public String getRegionInfo(SlingHttpServletRequest request) {
        String hostName;
        try {
            hostName = new URL(request.getRequestURL().toString()).getHost();

            log.info("Host Name resolved in provider is: " + hostName);

            if (StringUtils.isNotBlank(hostName)) {
                String[] hostnames = hostName.split(Pattern.quote("."));
                log.info("Hostname length after split by dot is: " + hostnames.length);

                if (hostnames.length > 2) {
                    return hostnames[1];
                }
            }
        } catch (MalformedURLException e) {
            log.error("Error occured while resolving the host name from the request URL: ",
                    e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

}
