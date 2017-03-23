package com.aaa.ace.servlets;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.services.SearchService;

/**
 * Article List Servlet.
 * 
 * Passes the input from Ajax call to service and returns back the JSON search
 * result.
 */

@Component(immediate = true, metatype = true, label = "AAA Artilce List Servlet")
@Service
@Properties(value = {
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default", propertyPrivate=true),
		@Property(name = "sling.servlet.selectors", value = "articles", propertyPrivate=true),
		@Property(name = "sling.servlet.extensions", value = "json", propertyPrivate=true),
		@Property(name = "sling.servlet.methods", value = "GET", propertyPrivate=true),
		@Property(name = "service.vendor", value = "com.aaa.ace.services"),
	    @Property(name = "service.description", value = "com.aaa.ace.services AAA Article Servlet")})

public class ArticleListServlet extends SlingAllMethodsServlet {
	
	private static final long serialVersionUID = 5L;

	public static final int DEFAULT_START_INDEX = 0;

	public static final String DEFAULT_NUM_OF_RESULT = "10";
	
	public static final String DEFAULT_TYPE = "jcr:content";
	
	public static final String CACHE_HEADER_NAME = "Cache-Control";
	
	public static final String CACHE_HEADER_SUFFIX = "public, max-age=";
	
	public static final String CACHE_HEADER_DEFAULT_MAX_AGE = "3200";
	
	//cache max age property 
    @Property(label = "Cache Header Max Age", 
    			description = "Response header Cache-Control Max Age value", 
    			unbounded = PropertyUnbounded.DEFAULT, value = "3200"
    			)
    private static final String RESPONSE_CACHE_CONTROL_MAX_AGE = "cache.max.age";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
    private String cacheHeader;
    
	@Reference
	SearchService searchService;

    @Activate
    protected void activate(ComponentContext ctx) {
        
        final Dictionary<?, ?> properties = ctx.getProperties();
        
        String maxage = PropertiesUtil.toString(properties.get(RESPONSE_CACHE_CONTROL_MAX_AGE), CACHE_HEADER_DEFAULT_MAX_AGE);
        
        cacheHeader = CACHE_HEADER_SUFFIX + maxage;
        
        log.debug("Max age is {}, Cache Header is {} ", maxage, cacheHeader);
    }
	
	@Override
	protected final void doGet(final SlingHttpServletRequest request,
			final SlingHttpServletResponse response) throws ServletException,
			IOException {

		// create query description as hash map (simplest way, same as form
		// post)
		Map<String, String> map = new HashMap<String, String>();
		
		String typeInput = request.getParameter("type");
		String type = isValid(typeInput) ? typeInput : DEFAULT_TYPE;

		String startIndexInput = request.getParameter("start");
		int startIndex = isValid(startIndexInput) ? Integer.parseInt(startIndexInput)-1 
				: DEFAULT_START_INDEX;

		String numOfResultInput = request.getParameter("num");
		String numOfResult = isValid(numOfResultInput) ? numOfResultInput
				: DEFAULT_NUM_OF_RESULT;
		
		map.put("type", type);
		map.put("p.hits", "full");
		map.put("orderby", "@issueDate");
		map.put("orderby.sort", "desc");
		map.put("p.offset", startIndex+"");
		map.put("p.limit", numOfResult);
		
		String filterInput = request.getParameter("filter");		
		
		if(isValid(filterInput))
		{
			map.put("2_property", "cq:tags");
			map.put("2_property.value", filterInput);
			map.put("2_property.operation", "equal");
		}
		
		//fire query using service and return the JSON
		String resultStr = "";
		
		try {
			
			resultStr = searchService.getSearchResultJSON(map);
			
		} catch (JSONException e) {
			log.error("JSONException in generating response ", e);
			resultStr = getErrorResponse(e);

		} catch (Exception e) {
			log.error("Exception in generating response ", e);
			resultStr = getErrorResponse(e);
		}
		
		
		
		log.debug("Setting Cache-Control header to " + cacheHeader);
		response.setHeader(CACHE_HEADER_NAME, cacheHeader);
		
		log.debug("Returning response: {}", resultStr);
		response.getWriter().write(resultStr);
	}
	
	/**
	 * Helper method to prepare error response.
	 * @param e
	 * @return
	 * @throws ServletException
	 */
	private String getErrorResponse(Exception e) throws ServletException {
		
		JSONObject returnJSON = new JSONObject();
		
		JSONObject error = new JSONObject();
		
		try {
			
			error.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			error.put("message", "Sorry, something went wrong, if issue persist please contact site adminstrator.");
			if (e.getCause() != null)
			{
				error.put("cause", e.getCause());
			}
			
			returnJSON.put("error", error);
		
		} catch (JSONException jsone) {

			log.error("Exception in generating error response ", jsone);
			
		}
		
		return returnJSON.toString();
	}

	/**
	 * Method to validate input. 
	 * @param input
	 * @return
	 */
	private boolean isValid(String input)
	{
		if (StringUtils.isBlank(input))
			return false;
		
		//The input we are expecting should not have < or > to prevent script injection.
		if (input.contains("<") || input.contains(">"))
		{
			log.debug("Invalid input: {} ", input);
			return false;
		}
		
		return true;
	}
	

}