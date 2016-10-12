package com.aaa.ace.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;

@Component(metatype = true,
        label = "AAA ACE - Site Map Servlet",
        description = "Page Site Map Servlet"
        )
@Service
@SuppressWarnings("serial")
@Properties(value = {
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.selectors", value = "sitemap"),
		@Property(name = "sling.servlet.extensions", value = "xml"),
		@Property(name = "sling.servlet.methods", value = "GET") 
})

public final class SiteMapServlet extends SlingSafeMethodsServlet {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final boolean DEFAULT_INCLUDE_LAST_MODIFIED = false;
    
    private static final String DEFAULT_HOME_PAGE = "/content/ace-www";

    @Property(label = "Site Home Page", unbounded = PropertyUnbounded.DEFAULT,
            description = "The path of site home page, this will be used as the root page and all the child node of this "
            		+ "path will be included in site map", value = DEFAULT_HOME_PAGE)
    private static final String PROP_SITE_HOME_PAGE = "site.root";

    @Property(boolValue = DEFAULT_INCLUDE_LAST_MODIFIED, label = "Include Last Modified",
            description = "If true, the last modified value will be included in the sitemap.")
    private static final String PROP_INCLUDE_LAST_MODIFIED = "include.lastmod";

    @Property(label = "Change Frequency Properties", unbounded = PropertyUnbounded.ARRAY,
            description = "The set of JCR property names which will contain the change frequency value.")
    private static final String PROP_CHANGE_FREQUENCY_PROPERTIES = "changefreq.properties";

    @Property(label = "Priority Properties", unbounded = PropertyUnbounded.ARRAY,
            description = "The set of JCR property names which will contain the priority value.")
    private static final String PROP_PRIORITY_PROPERTIES = "priority.properties";

    @Property(label = "Exclude from Sitemap Property",
            description = "The boolean [cq:Page]/jcr:content property name which indicates if the Page should be hidden from the Sitemap. Default value: hideInNav")
    private static final String PROP_EXCLUDE_FROM_SITEMAP_PROPERTY = "exclude.property";

    private static final String NS = "http://www.sitemaps.org/schemas/sitemap/0.9";
    
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private Externalizer externalizer;

    private boolean includeLastModified;

    private String[] changefreqProperties;

    private String[] priorityProperties;
    
    private String excludeFromSiteMapProperty;
    
    private String siteRootPath;

    @Activate
    protected void activate(Map<String, Object> properties) {
        this.includeLastModified = PropertiesUtil.toBoolean(properties.get(PROP_INCLUDE_LAST_MODIFIED), DEFAULT_INCLUDE_LAST_MODIFIED);
        this.changefreqProperties = PropertiesUtil.toStringArray(properties.get(PROP_CHANGE_FREQUENCY_PROPERTIES), new String[0]);
        this.priorityProperties = PropertiesUtil.toStringArray(properties.get(PROP_PRIORITY_PROPERTIES), new String[0]);
        this.excludeFromSiteMapProperty = PropertiesUtil.toString(properties.get(PROP_EXCLUDE_FROM_SITEMAP_PROPERTY), NameConstants.PN_HIDE_IN_NAV);
        this.siteRootPath = PropertiesUtil.toString(properties.get(PROP_SITE_HOME_PAGE), DEFAULT_HOME_PAGE);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
    	
        response.setContentType(request.getResponseContentType());
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource homeResource = resourceResolver.getResource(siteRootPath);
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page page = pageManager.getContainingPage(homeResource);

        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try {
            XMLStreamWriter stream = outputFactory.createXMLStreamWriter(response.getWriter());
            stream.writeStartDocument("1.0");

            stream.writeStartElement("", "urlset", NS);
            stream.writeNamespace("", NS);

            log.debug("Request local address " + request.getLocalAddr());
            
            // first do the current page
            write(page, stream, request);
            
            boolean includeInvalid = false;
            boolean includeHidden = true;
            
            for (Iterator<Page> children = page.listChildren(new PageFilter(includeInvalid, includeHidden), true); children.hasNext(); ) {
                write(children.next(), stream, request);
            }
            
            stream.writeEndElement();

            stream.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void write(Page page, XMLStreamWriter stream, SlingHttpServletRequest request) throws XMLStreamException {
        if (isExcluded(page)) {
            return;
        }
        stream.writeStartElement(NS, "url");

        String loc = externalizer.absoluteLink(request, request.getScheme(), String.format("%s.html", page.getPath())); 
        		
        writeElement(stream, "loc", loc);

        if (includeLastModified) {
            Calendar cal = page.getLastModified();
            if (cal != null) {
                writeElement(stream, "lastmod", DATE_FORMAT.format(cal));
            }
        }

        final ValueMap properties = page.getProperties();
        writeFirstPropertyValue(stream, "changefreq", changefreqProperties, properties);
        writeFirstPropertyValue(stream, "priority", priorityProperties, properties);

        stream.writeEndElement();
    }

    private boolean isExcluded(final Page page) {
        return page.getProperties().get(this.excludeFromSiteMapProperty, false);
    }

    private void writeFirstPropertyValue(final XMLStreamWriter stream, final String elementName, final String[] propertyNames,
                                         final ValueMap properties) throws XMLStreamException {
        for (String prop : propertyNames) {
            String value = properties.get(prop, String.class);
            if (value != null) {
                writeElement(stream, elementName, value);
                break;
            }
        }
    }

    private void writeElement(final XMLStreamWriter stream, final String elementName, final String text) throws XMLStreamException {
        stream.writeStartElement(NS, elementName);
        stream.writeCharacters(text);
        stream.writeEndElement();
    }

}