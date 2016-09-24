package com.aaa.ace.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;

import com.aaa.ace.services.SearchService;

/**
 * Article List Servlet.
 * 
 * Passes the input from Ajax call to service and returns back the JSON search
 * result.
 */
@Service
@Component
@Properties(value = {
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.selectors", value = "articles"),
		@Property(name = "sling.servlet.extensions", value = "json"),
		@Property(name = "sling.servlet.methods", value = "GET") })
public class ArticleListServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 5L;

	public static int DEFAULT_START_INDEX = 0;

	public static String DEFAULT_NUM_OF_RESULT = "10";

	@Reference
	SearchService searchService;

	@Override
	protected final void doGet(final SlingHttpServletRequest request,
			final SlingHttpServletResponse response) throws ServletException,
			IOException {

		// create query description as hash map (simplest way, same as form
		// post)
		Map<String, String> map = new HashMap<String, String>();

		// create query description as hash map (simplest way, same as form
		// post)
		map.put("path", "/content/ace-www");
		map.put("type", "jcr:content");
		map.put("1_property", "cq:template");
		map.put("1_property.value", "/apps/ace-www/templates/article");
		map.put("1_property.operation", "equal");
		map.put("p.hits", "full");
		map.put("orderby", "@issueDate");
		map.put("orderby.sort", "desc");

		// TODO: check for XSS security
		String startIndexInput = request.getParameter("start");
		int startIndex = StringUtils.isNotBlank(startIndexInput) ? (Integer.parseInt(startIndexInput)-1)
				: DEFAULT_START_INDEX;

		// TODO: check for XSS security
		String numOfResultInput = request.getParameter("num");
		String numOfResult = StringUtils.isNotBlank(numOfResultInput) ? numOfResultInput
				: DEFAULT_NUM_OF_RESULT;
		
		// can be done in map or with Query methods
		map.put("p.offset", startIndex+"");
		map.put("p.limit", numOfResult);
		
		// TODO: check for XSS security
		String filterInput = request.getParameter("filter");		
		
		if(StringUtils.isNotBlank(filterInput))
		{
			map.put("2_property", "cq:tags");
			map.put("2_property.value", filterInput);
			map.put("2_property.operation", "equal");
		}
		
		String path = request.getParameter("path");
		if (StringUtils.isNotEmpty(path))
		{
			map.put("path", path);
		}
		
		String type = request.getParameter("type");
		if (StringUtils.isNotEmpty(path))
		{
			map.put("type", type);
		}

		try {
			
			String resultStr = searchService.getSearchResultJSON(map);

			response.getWriter().write(resultStr);

		} catch (JSONException e) {
			throw new ServletException(e);

		} catch (Exception e) {
			throw new ServletException(e);

		}
	}

}