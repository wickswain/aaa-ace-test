<%--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~
  ~ ADOBE CONFIDENTIAL
  ~ __________________
  ~
  ~  Copyright 2013 Adobe Systems Incorporated
  ~  All Rights Reserved.
  ~
  ~ NOTICE:  All information contained herein is, and remains
  ~ the property of Adobe Systems Incorporated and its suppliers,
  ~ if any.  The intellectual and technical concepts contained
  ~ herein are proprietary to Adobe Systems Incorporated and its
  ~ suppliers and are protected by trade secret or copyright law.
  ~ Dissemination of this information or reproduction of this material
  ~ is strictly forbidden unless prior written permission is obtained
  ~ from Adobe Systems Incorporated.
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~--%>

<%@include file="/libs/foundation/global.jsp"%><%
%><%@page import="
	   com.day.cq.commons.JS,
	   com.day.cq.i18n.I18n,
	   com.day.cq.personalization.AreaService,
	   com.day.cq.personalization.TargetedContentManager,
	   com.day.cq.personalization.ClientContextUtil,
	   com.day.cq.personalization.Teaser,
	   com.day.cq.wcm.api.Page,
	   com.day.cq.wcm.api.WCMMode,
	   com.day.cq.wcm.core.stats.PageViewStatistics,
	   com.google.gson.GsonBuilder,
	   com.google.gson.JsonArray,
	   com.google.gson.JsonElement,
	   com.google.gson.JsonObject,
	   com.google.gson.JsonParser,
	   com.google.gson.JsonPrimitive,
	   com.google.gson.reflect.TypeToken,
	   java.io.StringWriter,
	   java.lang.reflect.Type,
	   java.net.URI,
	   java.net.URISyntaxException,
	   java.util.ArrayList,
	   java.util.HashSet,
	   java.util.List,
	   java.util.Set,
	   org.apache.commons.collections.CollectionUtils,
	   org.apache.commons.collections.Predicate,
	   org.apache.sling.api.resource.Resource,
	   org.apache.sling.api.resource.ResourceResolver,
	   org.apache.sling.api.SlingHttpServletRequest" %><%

    final I18n i18n = new I18n(slingRequest);
    final PageViewStatistics pageStats = sling.getService(PageViewStatistics.class);
    final TargetedContentManager targetedContentManager = sling.getService(TargetedContentManager.class);
    final AreaService areaService = sling.getService(AreaService.class);

    /* get resource info */
    final String campaignPath = properties.get("campaignpath", String.class);
    final String strategy = properties.get("strategyName", "");
    final String location = properties.get("location", resource.getPath());

    /* css class based on campaign name */
    String campaignClass = "";

    if (campaignPath != null) {
        Page campaignPage = pageManager.getPage(campaignPath);
        campaignClass = (campaignPage == null) ? "" : ("campaign-" + campaignPage.getName());
    }

    /* tracking url */
    URI trackingURI = (pageStats == null) ? null : pageStats.getTrackingURI();
    String trackingURL = (trackingURI == null) ? null : trackingURI.toString();

    /* teaser id */
    String targetDivId = ClientContextUtil.getId(resource.getPath());

    List<Teaser> teaserList = targetedContentManager.getTeasers(resourceResolver, location, slingRequest.getPathInfo());
    final Set<Resource> areas = areaService.getAreasForPage(currentPage.adaptTo(Resource.class));
    final Set<String> mappedAreaPaths = new HashSet<String>(areas.size());
    for(Resource area : areas){
        mappedAreaPaths.add(resourceResolver.map(area.getPath()));
    }
    final String filterPath = resourceResolver.map(resource.getPath());

    if (teaserList != null) {
        CollectionUtils.filter(teaserList, new Predicate() {

            public boolean evaluate(Object object) {
                if(object instanceof Teaser) {
                    Teaser teaser = (Teaser) object;
                    // Make sure default offer is never purged
                    if (teaser.getUrl().startsWith(filterPath)) {
                        return true;
                    } else if (areas != null) {
                        for (String areaPath : mappedAreaPaths) {
                            if (teaser.getUrl().startsWith(areaPath)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
    }


    Type listType = new TypeToken<ArrayList<Teaser>>() {}.getType();
    String teaserVariants = new GsonBuilder().create().toJson(teaserList, listType);
    if (teaserVariants.length() > 0) {
        String targetingEngine = (String)slingRequest.getAttribute("engine");
        if ("cq".equals(targetingEngine)) {
            String decoratedTeaserVariants = decorateTeaserUrl(slingRequest, teaserVariants);
%>
            <script type="text/javascript">
                $CQ(function() {
                    CQ_Analytics.Engine.loadTeaser({ targetID: <%= JS.str(targetDivId) %>,<%
                                                  %> teasers: <%= teaserVariants %>,<%
                                                  %> strategy: <%= JS.str(strategy) %>,<%
                                                  %> trackingURL: <%= JS.str(trackingURL) %>});
                });
            </script>
<%      } else if ("cq_contexthub".equals(targetingEngine)) {
            String decoratedTeaserVariants = decorateTeaserUrl(slingRequest, teaserVariants); %>
            <script type="text/javascript">
                if (window.ContextHub && ContextHub.SegmentEngine) {
                    ContextHubJQ(function() {
                        ContextHub.SegmentEngine.PageInteraction.Teaser({
                            locationId: '<%= xssAPI.encodeForJSString(targetDivId) %>',
                            variants: <%= decoratedTeaserVariants %>,
                            strategy: '<%= xssAPI.encodeForJSString(strategy) %>',
                            trackingURL: <%= (trackingURL != null) ? ("'" + xssAPI.encodeForJSString(trackingURL) + "'") : "null" %>
                        });
                    });
                }
            </script>
        <% } %>

        <div id="<%= xssAPI.encodeForHTMLAttr(targetDivId) %>" class="campaign <%= xssAPI.encodeForHTMLAttr(campaignClass) %>">
        <%
            if (resource.getChild("default") != null) {
                //include a default teaser into a noscript tag in case of no JS (SEO...)
                StringWriter defaultHtml = new StringWriter();
                pageContext.pushBody(defaultHtml);
                %><sling:include replaceSelectors="noscript" path="default"/><%
                pageContext.popBody();
        %><noscript><%=defaultHtml%></noscript><%
            } %>
        </div>

<%  } else if (WCMMode.fromRequest(request) == WCMMode.EDIT) { %>
        <style type="text/css">
            .cq-teaser-placeholder-off {
                display: none;
            }
        </style>
        <h3 class="cq-texthint-placeholder"><%=i18n.get("No active campaigns target this teaser") %></h3>
        <img src="/libs/cq/ui/resources/0.gif" class="cq-teaser-placeholder" alt="">
<% } %>

<%!
    private String decorateTeaserUrl(SlingHttpServletRequest request, String json) {
        JsonParser parser = new JsonParser();
        JsonArray elements = parser.parse(json).getAsJsonArray();

        JsonArray newArray = new JsonArray();
        String contextPath = request.getContextPath();
        for (JsonElement object: elements) {
            JsonObject element = object.getAsJsonObject();
            String teaserPath = contextPath + element.get("path").getAsString();;
            String url = contextPath + element.get("url").getAsString();

            try {
            	if (url.contains("http")) {
					url = new URI(url).getPath();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            element.add("path", new JsonPrimitive(teaserPath));
            element.add("url", new JsonPrimitive(url));
            newArray.add(element);
        }

        String decoratedTeaserVariants = newArray.toString();
        return decoratedTeaserVariants;
    }
%>
