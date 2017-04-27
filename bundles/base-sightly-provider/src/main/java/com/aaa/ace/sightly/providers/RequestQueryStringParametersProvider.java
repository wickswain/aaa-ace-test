package com.aaa.ace.sightly.providers;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.common.Constants;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider class is used to save the query string parameters in the cookie.
 * 
 */
public class RequestQueryStringParametersProvider extends WCMUsePojo {

	/**
	 * Logger object.
	 */
	private static final Logger log = LoggerFactory.getLogger(RequestQueryStringParametersProvider.class);

	@Override
	public void activate() throws Exception {
		Cookie queryStringCookie = null;
		String queryStringParams = getRequest().getQueryString();
		log.debug("Query String parameter :"+queryStringParams);
		queryStringCookie = new Cookie(Constants.QUERY_STRING_PARAMS_COOKIE, queryStringParams);
		queryStringCookie.setPath("/");

		if (StringUtils.isNotBlank(queryStringParams)) {
			queryStringCookie.setMaxAge(24 * 60 * 60);
		} else {
			queryStringCookie.setMaxAge(0);
		}

		getResponse().addCookie(queryStringCookie);
	}

}
