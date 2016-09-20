package com.aaa.ace.sightly.providers;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider class is used to provide the user logged in state.
 * 
 * @author bharath.kambam
 *
 */
public class UserLoginProvider extends WCMUsePojo {

    boolean isLoggedIn = false;
    String firstName;
    String lastName;

    @Override
    public void activate() throws Exception {
        Cookie logInCookie = getRequest().getCookie("aceu");

        if (logInCookie != null) {
            isLoggedIn = true;
            firstName = fetchCookieValue("aceuser", "FirstName");
            lastName = fetchCookieValue("aceuser", "LastName");

        }
    }

    /**
     * @param cookieName
     */
    private String fetchCookieValue(String cookieName, String key) {
        String cookieValue = "";
        Cookie aceuser = getRequest().getCookie(cookieName);

        if (aceuser != null && StringUtils.isNotBlank(aceuser.getValue())) {
            String[] cookieItems = aceuser.getValue().split("&");

            for (String cookieItem : cookieItems) {
                if (cookieItem.contains(key)) {
                    cookieValue = cookieItem.substring(cookieItem.indexOf("=") + 1);
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

}
