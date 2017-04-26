package com.aaa.ace.sightly.providers;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.common.Constants;
import com.aaa.ace.services.PageSuffixResolverService;
import com.aaa.ace.services.RegionDataService;
import com.aaa.ace.services.RunmodeProviderService;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * @author manish.singh
 *
 */
public class CTANavigationURLProvider extends WCMUsePojo {

    private static final Logger log = LoggerFactory.getLogger(CTANavigationURLProvider.class);

    private static final String SELECTED_QUERY_STRING_PROPERTY = "selectedQueryString";

    private static final String CONTENT_ROOT_PATH = "/content";

    private static final String URL_PROPERTY_NAME = "url";

    private static final String CHILD_RESOURCE = "childResource";

    private static final String QUERY_STRING_KEY = "queryStringKey";

    private static final String EQUALS_CHARACTER = "=";

    private static final String AMPERSAND_CHARACTER = "&";

    private static final String EXCLAMATION_CHARACTER = "?";

    private static final String COMMA_DELIMITER = ",";

    private static final String DOMAIN_PLACE_HOLDER = "[Domain]";

    private static final String CLUB_PLACE_HOLDER = "[ClubName]";
    
    private static final String TARGETER_COMPONENT_PATH ="/content/campaigns";
    
    private boolean isComponentTargeted = false;

	private String url;

    @Override
    public void activate() throws Exception {
        log.debug("Start of CTANavigationURLProvider class");

        String customQueryParamsString = get(QUERY_STRING_KEY, String.class);
        Resource childResource = get(CHILD_RESOURCE, Resource.class);
        url = get(URL_PROPERTY_NAME, String.class);
        
        if(childResource != null){
        	String resourecPath = childResource.getPath();
        	if(StringUtils.isNotBlank(resourecPath) && StringUtils.startsWith(resourecPath, TARGETER_COMPONENT_PATH))
        	{
        		isComponentTargeted=true;
        	} else {
        		
        		isComponentTargeted=false;
        	}
        }

        // Fetch the query string parameters selected from the available list
        Value[] selectedQueryParams = fetchQueryStringParameters(childResource);

        // Fetch the query string parameters defined as custom
        String[] customQueryParams = fetchCustomQueryStringParameters(customQueryParamsString);

        // Fetch valid URL.
        if (StringUtils.isNotBlank(url) && url.startsWith(CONTENT_ROOT_PATH)) {
            // Fetch valid internal URL.
            PageSuffixResolverService pageService = getSlingScriptHelper()
                    .getService(PageSuffixResolverService.class);

            url = pageService.resolveLinkMapURL(getResourceResolver(), url);
        } else if (StringUtils.isNotBlank(url) && url.contains(DOMAIN_PLACE_HOLDER)
                && url.contains(CLUB_PLACE_HOLDER)) {
            // Fetch valid AAA APP URL.
            RunmodeProviderService runmodeService = getSlingScriptHelper()
                    .getService(RunmodeProviderService.class);
            RegionDataService regionService = getSlingScriptHelper()
                    .getService(RegionDataService.class);

            String env = runmodeService.getEnvironmentInfo();
            String regionName = regionService.getRegionInfo(getRequest());

            url = getExternalAppURL(url, regionName, env);
        }

        // Fetch valid URL by appending the query parameters if any
        fetchURLWithQueryParams(selectedQueryParams, customQueryParams);

        log.debug("End of CTANavigationURLProvider class");
    }

    /**
     * Gets the valid URL by appending the query parameters if any.
     *
     * @param selectedQueryParams
     * @param customQueryParams
     */
    private void fetchURLWithQueryParams(Value[] selectedQueryParams, String[] customQueryParams) {
        if (StringUtils.isNotBlank(url)) {
            // Append the custom query parameters if any
            if (customQueryParams != null) {
                for (int i = 0; i < customQueryParams.length; i++) {
                    if (StringUtils.isNotBlank(customQueryParams[i].trim())) {
                        url = getQueryStringValueConcatenatedURL(customQueryParams[i].trim(), url);
                    }
                }
            }

            // Append the selected query parameters if any
            if (selectedQueryParams.length > 0) {
                for (int i = 0; i < selectedQueryParams.length; i++) {
                    if (StringUtils.isNotBlank(selectedQueryParams[i].toString().trim())) {
                        url = getQueryStringValueConcatenatedURL(
                                selectedQueryParams[i].toString().trim(), url);
                    }
                }
            }
        }
    }

    /**
     * Gets the custom query parameters array.
     *
     * @param customQueryParameterString
     * @param keyArray
     * @return
     */
    private String[] fetchCustomQueryStringParameters(String queryParameterString) {
        String[] queryParamsArray = null;

        if (StringUtils.isNotBlank(queryParameterString)) {
            log.debug("Custom query string parameters from component are: " + queryParameterString);
            queryParamsArray = queryParameterString.split(COMMA_DELIMITER);
        }

        return queryParamsArray;
    }

    /**
     * Gets the selected query parameters.
     * 
     * @param selectedCommonQueryStringKeyArray
     * @param childResource
     * @return
     * @throws RepositoryException
     * @throws PathNotFoundException
     * @throws ValueFormatException
     */
    private Value[] fetchQueryStringParameters(Resource childResource)
            throws RepositoryException, PathNotFoundException, ValueFormatException {
        Value[] selectedCommonQueryStringKeyArray = {};

        if (childResource != null) {
            log.debug("Child resource path: " + childResource.getPath());
            Node propertiesNode = childResource.adaptTo(Node.class);

            if (propertiesNode.hasProperty(SELECTED_QUERY_STRING_PROPERTY)) {
                Property selectedCommonQuery = propertiesNode
                        .getProperty(SELECTED_QUERY_STRING_PROPERTY);

                if (selectedCommonQuery != null) {
                    if (selectedCommonQuery.isMultiple()) {
                        selectedCommonQueryStringKeyArray = selectedCommonQuery.getValues();
                    } else {
                        Value[] commonQuery = { selectedCommonQuery.getValue() };
                        selectedCommonQueryStringKeyArray = commonQuery;
                    }
                }
            }
        }

        return selectedCommonQueryStringKeyArray;
    }

	/**
	 * Gets the final valid URL by appending the query parameters.
	 *
	 * @param key
	 * @param url
	 * @return
	 */
	private String getQueryStringValueConcatenatedURL(String key, String url) {
		log.debug("Start of getQueryStringValueConcatenatedURL method");

		if (isComponentTargeted) {

			Cookie queryStringCookie = getRequest().getCookie(Constants.QUERY_STRING_PARAMS_COOKIE);
			if (StringUtils.isNotBlank(key) && queryStringCookie != null) {
				Map<String, String> queryStringParams = fetchCookieValues(queryStringCookie);

				if (queryStringParams.containsKey(key)) {
					String queryStringValue = queryStringParams.get(key);
					log.debug("queryStringValue: " + queryStringValue);

					url = (url.contains(EXCLAMATION_CHARACTER) ? url.concat(AMPERSAND_CHARACTER)
							: url.concat(EXCLAMATION_CHARACTER)).concat(key).concat(EQUALS_CHARACTER)
									.concat(queryStringValue);
				}
			}

		} else if (StringUtils.isNotBlank(key) && getRequest().getParameter(key) != null) {

			log.debug("parameter key :" + getRequest().getParameter(key));
			String queryStringValue = getRequest().getParameter(key);
			log.debug("queryStringValue: " + queryStringValue);

			url = (url.contains(EXCLAMATION_CHARACTER) ? url.concat(AMPERSAND_CHARACTER)
					: url.concat(EXCLAMATION_CHARACTER)).concat(key).concat(EQUALS_CHARACTER).concat(queryStringValue);

		} else {
			log.debug("No query string parameters found on request");
		}

		log.debug("Query String Value Concatenated Final URL: " + url);

		return url;
	}

	/**
	 * Gets the cookie values in Map
	 *
	 * @param cookieName
	 */
	private Map<String, String> fetchCookieValues(Cookie cookie) {
		Map<String, String> cookieValues = new HashMap<String, String>();

		if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
			String[] cookieItems = cookie.getValue().split(Constants.STRING_AND_SYMBOL);

			for (String cookieItem : cookieItems) {
				String[] queryParam = cookieItem.split("=");
				cookieValues.put(queryParam[0].trim(), queryParam[1].trim());
			}
		}

		return cookieValues;
	}

    /**
     * Gets the valid AAA APP URL.
     *
     * @param url
     * @param regionName
     * @param env
     * @return
     */
    public String getExternalAppURL(String url, String regionName, String env) {
        log.debug("Start of getExternalAppURL method");
        log.debug("URL: " + url);
        log.debug("Region: " + regionName);
        log.debug("Env: " + env);

        if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(regionName)
                && StringUtils.isNotBlank(env)) {
            return url.replace(CTANavigationURLProvider.DOMAIN_PLACE_HOLDER, env)
                    .replace(CTANavigationURLProvider.CLUB_PLACE_HOLDER, regionName);
        } else {
            log.error(
                    "Error: Either URL or RegionName or Env received from component is empty or null");
        }

        log.debug("Final URL after replacing the placeholder: " + url);

        return url;
    }

    /**
     * Gets the final URL.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    
    /**
     *
     * @return the isComponentTargeted
     */
    public boolean isComponentTargeted() {
		return isComponentTargeted;
	}

}
