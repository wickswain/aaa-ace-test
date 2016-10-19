package com.aaa.ace.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;

import com.aaa.ace.common.Constants;

/**
 * User Login Servlet.
 * 
 * Write login status of the user.
 */
@Service
@Component
@Properties(value = {
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.selectors", value = "loginstatus"),
        @Property(name = "sling.servlet.extensions", value = "json"),
        @Property(name = "sling.servlet.methods", value = "GET") })
public class UserLoginServlet extends SlingAllMethodsServlet {

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected final void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {

        response.setContentType(Constants.JSON_CONTENT_TYPE);
        response.setCharacterEncoding(Constants.CHARSET_NAME);

        try {
            boolean isLoggedIn = false;
            Cookie logInCookie = request.getCookie(Constants.COOKIE_ASPXAUTH);

            if (logInCookie != null) {
                isLoggedIn = true;
            }

            final JSONWriter jsonWriter = new JSONWriter(response.getWriter());

            jsonWriter.object();
            jsonWriter.key("isLoggedIn").value(isLoggedIn);
            jsonWriter.endObject();

        } catch (JSONException e) {
            throw new ServletException(e);
        }
    }

}
