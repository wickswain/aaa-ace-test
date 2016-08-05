package com.aaa.ace.services.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;

import com.aaa.ace.services.PageSuffixResolverService;
import com.day.cq.wcm.api.Page;

/*class to resolve URL suffix for a Page Resource, if it is a JCR path, .html would be appended as suffix
 * 
 */
/**
* @author manish.singh
*
*/
@Component
@Service
@Properties({
       @Property(name = "service.vendor", value = "com.aaa.ace.services"),
       @Property(name = "service.description", value = "com.aaa.ace.services Page Suffix Resolver Service"), })
public class PageSuffixResolverServiceImpl implements PageSuffixResolverService{
	
	 private static final String PAGE_SUFFIX = ".html";

	/**
     * Validate the URL and append .html extension if it is an AEM page.
     *
     * @param resolver
     *            resource resolver to resolve the link
     * @param link
     *            resource link to be resolved
     * @return String
     */
	@Override
	public String resolveLinkURL(ResourceResolver resolver, String link) {
		if (!ResourceUtil.isNonExistingResource(resolver.getResource(link))) {
            if (resolver.getResource(link).adaptTo(Page.class) != null) {
                return link.concat(PAGE_SUFFIX);
            }
        }
		return link;
	}
}
