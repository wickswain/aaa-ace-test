package com.aaa.ace.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.AEMUtilityService;

@Service(value = AEMUtilityService.class)
@Component(metatype = false, immediate = false)
public class AEMUtilityServiceImpl implements AEMUtilityService {

    /** Default log. **/
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public Session getSystemUserMapperServiceSession(String subServiceName) {
        log.debug("Entered into getSystemUserMapperServiceSession method");

        if (StringUtils.isNotBlank(subServiceName)) {
            try {
                Map<String, Object> param = new HashMap<String, Object>();

                param.put(ResourceResolverFactory.SUBSERVICE, subServiceName);
                ResourceResolver userMapperServiceResolver = resourceResolverFactory
                        .getServiceResourceResolver(param);

                if (userMapperServiceResolver != null) {
                    return userMapperServiceResolver.adaptTo(Session.class);
                }
            } catch (LoginException e) {
                log.error("Could not get admin session from user mapper service: " + e.toString());
                e.printStackTrace();
            }
        }

        return null;
    }

}
