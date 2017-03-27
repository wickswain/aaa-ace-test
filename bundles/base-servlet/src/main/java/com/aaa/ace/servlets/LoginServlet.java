package com.aaa.ace.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.common.Constants;

/**
 * Login servlet.
 * 
 * Returns the Login user personal information
 * 
 * @author Sridhar M.
 */

@SlingServlet(paths = { "/bin/aaa/userlogin" }, methods = { "GET", "POST" })
@Properties({ @Property(name = "service.pid", value = "com.aaa.ace.servlets.LoginServlet", propertyPrivate = false),
		@Property(name = "service.description", value = "Login Servlet", propertyPrivate = false) })
public class LoginServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final String STRING_EQUAL_SYMBOL = "=";
	private static final String STRING_AND_SYMBOL = "&";
	private static final String PROPERTY_LAST_NAME = "LastName";
	private static final String PROPERTY_FIRST_NAME = "FirstName";
	private static final String COOKIE_ACEUSER = "aceuser";
	private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		boolean isLoggedIn = false;
		String firstName = null;
		String lastName = null;
		JSONObject obj = new JSONObject();
		Cookie logInCookie = request.getCookie(Constants.COOKIE_ASPXAUTH);

		log.info("Start LoginServlet User loggined :{}", logInCookie);

		if (logInCookie != null) {
			isLoggedIn = true;
			firstName = fetchCookieValue(COOKIE_ACEUSER, PROPERTY_FIRST_NAME, request);
			lastName = fetchCookieValue(COOKIE_ACEUSER, PROPERTY_LAST_NAME, request);
		}
		try {
			obj.put("isLoggedIn", isLoggedIn);
			obj.put("firstName", firstName);
			obj.put("lastName", lastName);

		} catch (Exception e) {
			log.error("Error occurred in creating json :{}", e.getMessage());
		}
		// Get the JSON formatted data
		String jsonData = obj.toString();
		response.setContentType("application/json");

		log.info("End LoginServlet, response :{}", jsonData);

		// Returns the user information
		response.getWriter().write(jsonData);
	}

	/**
	 * @param cookieName
	 */
	private String fetchCookieValue(String cookieName, String key, SlingHttpServletRequest request) {
		String cookieValue = "";
		Cookie aceuser = request.getCookie(cookieName);

		log.info("Entered fetchCookieValue()");

		if (aceuser != null && StringUtils.isNotBlank(aceuser.getValue())) {
			String[] cookieItems = aceuser.getValue().split(STRING_AND_SYMBOL);

			for (String cookieItem : cookieItems) {
				if (cookieItem.contains(key)) {
					cookieValue = cookieItem.substring(cookieItem.indexOf(STRING_EQUAL_SYMBOL) + 1);
				}
			}
		}

		log.info("End  fetchCookieValue(), cookieValue:{}", cookieValue);
		return cookieValue;
	}
}