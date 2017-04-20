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

import com.aaa.ace.services.PageSuffixResolverService;
import com.aaa.ace.services.RegionDataService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;

/**
 * The SiteMapServlet class defined to render the SiteMap structure for the
 * given root of the site.
 *
 * @author bharath.kambam
 *
 */
@Component(metatype = true, label = "AAA ACE - Site Map Servlet", description = "Render the SiteMap structure for the site in XML format.")
@Service
@SuppressWarnings("serial")
@Properties(value = {
    @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default", propertyPrivate = true),
    @Property(name = "sling.servlet.selectors", value = "sitemap", propertyPrivate = true),
    @Property(name = "sling.servlet.extensions", value = "xml", propertyPrivate = true),
    @Property(name = "sling.servlet.methods", value = "GET", propertyPrivate = true)
})
public final class SiteMapServlet extends SlingSafeMethodsServlet {

    /**
     * Logger constant variable.
     */
    private static final Logger logger = LoggerFactory.getLogger(SiteMapServlet.class);

    /**
     * Date format constant variable (yyyy-MM-dd).
     */
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    /**
     * Include last modified default flag constant.
     */
    private static final boolean DEFAULT_INCLUDE_LAST_MODIFIED = false;

    /**
     * Home page default constant.
     */
    private static final String DEFAULT_HOME_PAGE = "/content/ace-www";

    /**
     * Hide in SiteMap property.
     */
    private static final String DEFAULT_HIDE_IN_SITEMAP_PROPERTY = "hideInSitemap";

    /**
     * NameSpace constant.
     */
    private static final String NS = "http://www.sitemaps.org/schemas/sitemap/0.9";

    /**
     * Include last modified flag.
     */
    private boolean includeLastModified;

    /**
     * Change frequency properties array.
     */
    private String[] changefreqProperties;

    /**
     * Priority properties array.
     */
    private String[] priorityProperties;

    /**
     * Exclude from SiteMap property.
     */
    private String excludeFromSiteMapProperty;

    /**
     * Site root path variable.
     */
    private String siteRootPath;

    @Property(label = "Site Home Page", unbounded = PropertyUnbounded.DEFAULT, description = "The path of site home page, this will be used as the root page" +
        " and all the child node of this " + " path will be included in site map.", value = DEFAULT_HOME_PAGE)
    private static final String PROP_SITE_HOME_PAGE = "site.root";

    @Property(boolValue = DEFAULT_INCLUDE_LAST_MODIFIED, label = "Include Last Modified", description = "If true, the last modified value will be " +
        "included in the sitemap.")
    private static final String PROP_INCLUDE_LAST_MODIFIED = "include.lastmod";

    @Property(label = "Change Frequency Properties", unbounded = PropertyUnbounded.ARRAY, description = "The set of JCR property names which will contain " +
        "the change frequency value.")
    private static final String PROP_CHANGE_FREQUENCY_PROPERTIES = "changefreq.properties";

    @Property(label = "Priority Properties", unbounded = PropertyUnbounded.ARRAY, description = "The set of JCR property names which will contain " +
        "the priority value.")
    private static final String PROP_PRIORITY_PROPERTIES = "priority.properties";

    @Property(label = "Exclude from Sitemap Property", description = "The boolean [cq:Page]/jcr:content property name which indicates " +
        "if the Page should be hidden from the Sitemap. " + "Default value: hideInNav")
    private static final String PROP_EXCLUDE_FROM_SITEMAP_PROPERTY = "exclude.property";

    @Reference
    private PageSuffixResolverService pageResolver;

    @Reference
    private RegionDataService regionDataService;

    /**
     * Read and assign the configured properties on service.
     *
     * @param properties
     */
    @Activate
    protected void activate(Map < String, Object > properties) {
        logger.info("Start of activate method in SiteMap servlet.");

        this.includeLastModified = PropertiesUtil.toBoolean(properties.get(PROP_INCLUDE_LAST_MODIFIED),
            DEFAULT_INCLUDE_LAST_MODIFIED);
        this.changefreqProperties = PropertiesUtil.toStringArray(properties.get(PROP_CHANGE_FREQUENCY_PROPERTIES),
            new String[0]);
        this.priorityProperties = PropertiesUtil.toStringArray(properties.get(PROP_PRIORITY_PROPERTIES), new String[0]);
        this.excludeFromSiteMapProperty = PropertiesUtil.toString(properties.get(PROP_EXCLUDE_FROM_SITEMAP_PROPERTY),
            DEFAULT_HIDE_IN_SITEMAP_PROPERTY);
        this.siteRootPath = PropertiesUtil.toString(properties.get(PROP_SITE_HOME_PAGE), DEFAULT_HOME_PAGE);

        logger.info("End of activate method in SiteMap servlet.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.
     * sling.api.SlingHttpServletRequest,
     * org.apache.sling.api.SlingHttpServletResponse)
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
    throws ServletException, IOException {
        logger.info("Start of doGet method in SiteMap servlet.");

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

             /*check for hide in sitemap property based on region in request and
             than write the current page*/
            if (checkForHideInSiteMap(page, request)) {
                write(page, stream, request);
            }

            boolean includeInvalid = false;
            boolean includeHidden = true;

            for (Iterator < Page > children = page.listChildren(new PageFilter(includeInvalid, includeHidden),
                    true); children.hasNext();) {
                Page childPage = children.next();
                if (checkForHideInSiteMap(childPage, request)) {
                    write(childPage, stream, request);
                }
            }

            stream.writeEndElement();

            stream.writeEndDocument();
        } catch (XMLStreamException e) {
            logger.error("Error occured during the XML creation." + e);
            throw new IOException(e);
        }

        logger.info("End of doGet method in SiteMap servlet.");
    }
    
    /**
     * Check for the region in property based on region in request to write the page
     *  
     * @param page
     * @param request
     */
    private boolean checkForHideInSiteMap(Page page, SlingHttpServletRequest request) {

        String regionInRequest = regionDataService.getRegionInfo(request);
        String regionInProperty = page.getProperties().get(regionInRequest + "-region", String.class);

        if (regionInProperty != null && regionInProperty.equalsIgnoreCase("true")) {
            return false;
        }
        return true;
    }

    /**
     * Write the SiteMap in XML format.
     *
     * @param page
     * @param stream
     * @param request
     * @throws XMLStreamException
     */
    private void write(Page page, XMLStreamWriter stream, SlingHttpServletRequest request) throws XMLStreamException {
        // filter the pages
        if (isExcluded(page)) {
            return;
        }
        stream.writeStartElement(NS, "url");

        String location = pageResolver.resolveLinkMapURL(request.getResourceResolver(), page.getPath());
        String regionInURL = location.substring(location.indexOf(".") + 1,
            location.indexOf(".", location.indexOf(".") + 1));
        String regionInRequest = regionDataService.getRegionInfo(request);

        logger.debug("Region in URL:" + regionInURL);
        logger.debug("Region In request:" + regionInRequest);

        location = location.replace(regionInURL, regionInRequest);

        logger.debug("Location path in sitemap servlet after manipulation:" + location);

        writeElement(stream, "loc", location);

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

    /**
     * Check for the valid pages.
     *
     * @param page
     * @return
     */
    private boolean isExcluded(final Page page) {
        return page.getProperties().get(this.excludeFromSiteMapProperty, false);
    }

    /**
     * Write first property value in XML.
     *
     * @param stream
     * @param elementName
     * @param propertyNames
     * @param properties
     * @throws XMLStreamException
     */
    private void writeFirstPropertyValue(final XMLStreamWriter stream, final String elementName,
        final String[] propertyNames, final ValueMap properties) throws XMLStreamException {
        for (String prop: propertyNames) {
            String value = properties.get(prop, String.class);
            if (value != null) {
                writeElement(stream, elementName, value);
                break;
            }
        }
    }

    /**
     * Construct element.
     *
     * @param stream
     * @param elementName
     * @param text
     * @throws XMLStreamException
     */
    private void writeElement(final XMLStreamWriter stream, final String elementName, final String text)
    throws XMLStreamException {
        stream.writeStartElement(NS, elementName);
        stream.writeCharacters(text);
        stream.writeEndElement();
    }

}