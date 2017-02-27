package com.aaa.ace.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aaa.ace.config.CORSConfig;
import com.day.cq.commons.Externalizer;

@SlingFilter(label = "AAA Cross-Origin Request Filter", metatype = true, scope = SlingFilterScope.REQUEST, order = -100)

public class CORSfilter implements Filter {

	/** default log. */
	private static final Logger LOG = LoggerFactory.getLogger(CORSfilter.class);
	
	private static final int DEFAULT_MAX_AGE = 3600;

	/** Configuration property to enable CORS requests */
	@Property(boolValue = false, label = "Enable CORS?", description = "Please check for enabling CORS for the following domains")
	private static final String CORS_ENABLED = "cors.enabled";

	/** Configuration to allow requests from specific domains */
	@Property(label = "Allowed Origins (cross domains)", description = "Please Enter http://:", unbounded = PropertyUnbounded.ARRAY)
	private static final String CORS_ALOOWED_ORIGINS = "cors.allowed.origins";

	
	/** Configuration to allow specific HTTP methods only */
	@Property(label = "Allowed HTTP Methods", description = "Add methods to be allowed like GET, POST etc.", unbounded = PropertyUnbounded.ARRAY)
	private static final String CORS_ALOOWED_METHODS = "cors.allowed.methods";


	/** Configuration to allow specific HTTP methods only */
	@Property(label = "Cache Time", description = "Value of Access-Control-Max-Age, to control browser cache time")
	private static final String CORS_MAX_AGE = "cors.max.age";
	
	
	/** The Externalizer. */
	@Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
	private Externalizer externalizer;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOG.debug("Initialising CORS Filter");
	}

	/**
	 * Component Activation.
	 * 
	 * @param context component context
	 */
	@Activate
	protected void activate(final ComponentContext context) {

		LOG.info("Activating Class {} ", getClass().getName());

		final Dictionary<String, Object> props = context.getProperties();
		final Object corsenabledProp = props.get(CORS_ENABLED);
		final String[] corsallowedoriginsProp = PropertiesUtil.toStringArray(props.get(CORS_ALOOWED_ORIGINS));
		final String[] corsallowedmethodsProp = PropertiesUtil.toStringArray(props.get(CORS_ALOOWED_METHODS));
		final CORSConfig corsConfig;
		
		if (corsenabledProp != null || corsallowedoriginsProp != null) {
			corsConfig = CORSConfig.getInstance();
			corsConfig.setEnabled(Boolean.TRUE.equals(corsenabledProp) ? true	: false);
			
			if (corsallowedoriginsProp != null	&& corsallowedoriginsProp.length > 0)
				corsConfig.setAllowedDomains(corsallowedoriginsProp);

			if (corsallowedmethodsProp != null	&& corsallowedmethodsProp.length > 0)
				corsConfig.setAllowedMethods(corsallowedmethodsProp);
			
			corsConfig.setMaxage(PropertiesUtil.toInteger(props.get(CORS_MAX_AGE), DEFAULT_MAX_AGE));
			
			LOG.debug("CORSConfig Object {} created", corsConfig.toString());
		}

	}

	/**
	 * Do filter method implementation.
	 * 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		LOG.info("Executing AAA CORS Filter  ");
		
		if (request instanceof SlingHttpServletRequest
				&& response instanceof SlingHttpServletResponse) {
			
			final CORSConfig corsConfig = CORSConfig.getInstance();
			
			LOG.info("CORS enabled? {}", corsConfig.isEnabled());
			
			if (corsConfig.isEnabled()) {
				final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
				final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
				final String localOrigin = externalizer.absoluteLink(slingRequest, slingRequest.getScheme(), "");
				final String origin = slingRequest.getHeader("Origin");
				
				LOG.debug("Local Origin {} ", localOrigin);
				
				LOG.debug("Origin from slingRequest.getHeader(Origin) {}", origin);
				
				LOG.debug("Requested Path {}", slingRequest.getRequestPathInfo().toString());
				
				// Check for Preflight request from cross origin
				if (slingRequest.getMethod().equals("OPTIONS") && corsConfig.getAllowedDomains() != null 
								&& ( Arrays.asList(corsConfig.getAllowedDomains()).contains(origin)
										|| Arrays.asList(corsConfig.getAllowedDomains()).contains("*"))
								&& !localOrigin.equals(origin)) {
					
					LOG.debug("Processing Preflight OPTIONS for Origin {} ", origin);
					
					
					slingResponse.setHeader("Access-Control-Allow-Origin", origin);
					slingResponse.setHeader("Access-Control-Allow-Credentials", "true");
					slingResponse.setHeader("Access-Control-Allow-Methods",	getAllowedMethodsString(corsConfig.getAllowedMethods()));
					slingResponse.addHeader("Access-Control-Allow-Headers",
									"X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
					slingResponse.addIntHeader("Access-Control-Max-Age", corsConfig.getMaxage());
					
					LOG.debug("Set response header for OPTIONS (preflight) for origin  {} "
							+ "and not flowing through filter chain", origin);
					return;
				}
				
				// Check for cors requests and allow
				if (origin != null && origin.length() > 0  //origin is present 
						&& !localOrigin.equals(origin) // and not equals to the current domain
						&& corsConfig.getAllowedDomains() != null 
						&& ( Arrays.asList(corsConfig.getAllowedDomains()).contains(origin)
								|| Arrays.asList(corsConfig.getAllowedDomains()).contains("*"))) // and allowed  
				{
					slingResponse.setHeader("Access-Control-Allow-Origin", origin);
					slingResponse.setHeader("Access-Control-Allow-Credentials",	"true");
					slingResponse.setHeader("Access-Control-Allow-Methods", getAllowedMethodsString(corsConfig.getAllowedMethods()));
					slingResponse.addHeader("Access-Control-Allow-Headers",
									"X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
					slingResponse.addIntHeader("Access-Control-Max-Age", corsConfig.getMaxage());
					
					LOG.debug("Set response header for origin {} and flowing through filter chain", origin);
				}
			}
		}

		chain.doFilter(request, response);
	}
	
	/**
	 * Utility method to get comma separated methods list from string array
	 * @param methods
	 * @return
	 */
	private String getAllowedMethodsString(String [] methods) {
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<methods.length; i++)
		{
			sb.append(StringUtils.upperCase(methods[i]));
			if( i != methods.length)
			{
				sb.append(",");
			}
		}
		
		LOG.debug("CORS methods " + sb.toString());
		return sb.toString();
	}


	// @Override
	public void destroy() {
		LOG.debug("Destroying CORS Filter");
	}
}