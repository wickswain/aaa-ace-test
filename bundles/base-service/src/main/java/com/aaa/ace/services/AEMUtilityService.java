package com.aaa.ace.services;

import javax.jcr.Session;

/**
 * The AEM Utility Service provides all the utility functionalities combined as
 * a service.
 *
 * @author bharath.kambam
 *
 */
public interface AEMUtilityService {

    /**
     * Gets the system user session.
     *
     * @return
     */
    public Session getSystemUserMapperServiceSession(String subServiceName);

}
