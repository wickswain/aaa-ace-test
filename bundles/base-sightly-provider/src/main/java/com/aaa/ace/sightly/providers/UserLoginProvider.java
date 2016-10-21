package com.aaa.ace.sightly.providers;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.common.Constants;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider class is used to provide the user logged in state.
 * 
 * @author bharath.kambam
 *
 */
public class UserLoginProvider extends WCMUsePojo {

    private static final Logger log = LoggerFactory.getLogger(UserLoginProvider.class);

    private static final String STRING_EQUAL_SYMBOL = "=";
    private static final String STRING_AND_SYMBOL = "&";
    private static final String PROPERTY_LAST_NAME = "LastName";
    private static final String PROPERTY_FIRST_NAME = "FirstName";
    private static final String COOKIE_ACEUSER = "aceuser";

    boolean isLoggedIn = false;
    String firstName;
    String lastName;
    String signInURL;
    String signOutURL;

    @Override
    public void activate() throws Exception {
        String requestURL = getRequestURL(getRequest());
        log.debug("Request URL:" + requestURL);

        signInURL = constructURL(get("signInURL", String.class), requestURL,
                getCurrentPage().getPath());
        log.debug("SignIN URL:" + signInURL);

        signOutURL = constructURL(get("signOutURL", String.class), requestURL,
                getCurrentPage().getPath());
        log.debug("SignOut URL:" + signOutURL);

        Cookie logInCookie = getRequest().getCookie(Constants.COOKIE_ASPXAUTH);

        if (logInCookie != null) {
            isLoggedIn = true;
            firstName = fetchCookieValue(COOKIE_ACEUSER, PROPERTY_FIRST_NAME);
            lastName = fetchCookieValue(COOKIE_ACEUSER, PROPERTY_LAST_NAME);

        }
    }

    /**
     * Gets the request URL by appending the request scheme, host and port.
     *
     * @param request
     * @return
     */
    private String getRequestURL(SlingHttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName).append(":").append(serverPort);

        return url.toString();
    }

    /**
     * Construct the Final URL.
     *
     * @param url
     * @param requestURL
     * @param currentPagePath
     * @return
     */
    private String constructURL(String url, String requestURL, String currentPagePath) {
        String finalURL = "";
        log.debug("URL from CTA Provider: " + url);

        if (StringUtils.isNotBlank(url)) {
            finalURL = url + "?ReturnURL=" + requestURL + currentPagePath;
            finalURL = (currentPagePath.endsWith(".html") ? finalURL : (finalURL + ".html"));
        }
        return finalURL;
    }

    /**
     * @param cookieName
     */
    private String fetchCookieValue(String cookieName, String key) {
        String cookieValue = "";
        Cookie aceuser = getRequest().getCookie(cookieName);

        if (aceuser != null && StringUtils.isNotBlank(aceuser.getValue())) {
            String[] cookieItems = aceuser.getValue().split(STRING_AND_SYMBOL);

            for (String cookieItem : cookieItems) {
                if (cookieItem.contains(key)) {
                    cookieValue = cookieItem.substring(cookieItem.indexOf(STRING_EQUAL_SYMBOL) + 1);
                }
            }
        }

        return cookieValue;
    }

    /**
     * @return the isLoggedIn
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the signInURL
     */
    public String getSignInURL() {
        return signInURL;
    }

    /**
     * @return the signOutURL
     */
    public String getSignOutURL() {
        return signOutURL;
    }

}
