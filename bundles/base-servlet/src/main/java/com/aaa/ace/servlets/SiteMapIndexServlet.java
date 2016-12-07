package com.aaa.ace.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.osgi.PropertiesUtil;

@Component(metatype = true, label = "AAA ACE - Site Map Index Servlet",
                description = "Region Site Map Index Servlet", configurationFactory = true)
@Service
@SuppressWarnings("serial")
@Properties({
                @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default",
                                                                    propertyPrivate = true),
                @Property(name = "sling.servlet.selectors", value = "sitemapindex",
                                                                    propertyPrivate = true),
                @Property(name = "sling.servlet.extensions", value = "xml", 
                                                                    propertyPrivate = true),
                @Property(name = "sling.servlet.methods", value = "GET", 
                                                                    propertyPrivate = true),
                @Property(name = "webconsole.configurationFactory.nameHint",
                                value = "Site Map index for root - aaa.com ") })
/**
 * Servlet to generate sitemap index.
 * @author yogesh.mahajan
 *
 */
public final class SiteMapIndexServlet extends SlingSafeMethodsServlet {

    private static final String SCHEME_SEPARATOR = "://";

    private static final String NS = "http://www.sitemaps.org/schemas/sitemap/0.9";

    @Property(
                    label = "URL Pattern",
                    unbounded = PropertyUnbounded.DEFAULT,
                    description = "The URL pattern which should be used to "
                                    + "generate region spacific paths. "
                                    + "Use <region> as placeholder for example "
                                    + "www.<region>.aaa.com/<region>.sitemap.xml, "
                                    + "the scheme (http/https) need not be configured.",
                    value = "www.<region>.aaa.com/<region>.sitemap.xml")
    private static final String PROP_URL_PATTERN = "url.pattern";

    @Property(label = "Regions", unbounded = PropertyUnbounded.ARRAY,
                    description = "Set of regions for which sitemap URL should be generated.",
                    value = { "calif", "hawaii", "newmexico", "texas", "northernnewengland",
                                    "alabama", "tidewater", "autoclubmo", "eastcentral" })
    private static final String PROP_REGIONS = "regions";

    private static final String REGION_PLACEHOLDER = "<region>";

    private String[] regions;

    private String urlPattern;

    @Activate
    protected void activate(Map<String, Object> properties) {
        this.regions = PropertiesUtil.toStringArray(properties.get(PROP_REGIONS), new String[0]);
        this.urlPattern = PropertiesUtil.toString(properties.get(PROP_URL_PATTERN),
                        "www.<region>.aaa.com/<region>.sitemap.xml");
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
                    throws ServletException, IOException {

        response.setContentType(request.getResponseContentType());

        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try {
            XMLStreamWriter stream = outputFactory.createXMLStreamWriter(response.getWriter());
            stream.writeStartDocument("1.0");

            stream.writeStartElement("", "urlset", NS);
            stream.writeNamespace("", NS);

            for (String region : regions) {
                write(region, stream, request.getScheme());
            }

            stream.writeEndElement();

            stream.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void write(String region, XMLStreamWriter stream, String scheme)
                    throws XMLStreamException {

        stream.writeStartElement(NS, "url");

        String loc = scheme + SCHEME_SEPARATOR
                        + StringUtils.replace(this.urlPattern, REGION_PLACEHOLDER, region);

        writeElement(stream, "loc", loc);

        stream.writeEndElement();
    }

    private void writeElement(final XMLStreamWriter stream, final String elementName,
                    final String text) throws XMLStreamException {
        stream.writeStartElement(NS, elementName);
        stream.writeCharacters(text);
        stream.writeEndElement();
    }

}