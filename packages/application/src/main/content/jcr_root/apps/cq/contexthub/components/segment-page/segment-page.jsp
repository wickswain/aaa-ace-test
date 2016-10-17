<%--

  ADOBE CONFIDENTIAL
  __________________

   Copyright 2015 Adobe Systems Incorporated
   All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.

--%><%@page
    session="false"
    pageEncoding="utf-8"
%><%@include file="/libs/cq/contexthub/global.jsp" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%

    /* constants */
    final String TRAITS_NODE = "traits";

    /* get segment details */
    String segmentPath = resource.getPath().replaceAll("/jcr:content$", "");
    String segmentName = properties.get("segmentName", "");
    String segmentTitle = properties.get("./jcr:title", segmentName);
    long segmentBoost = properties.get("segmentBoost", 0L);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
    <head>
<!-- custom code starts here -->
<script src="//tags.tiqcdn.com/utag/aaa/main/prod/utag.sync.js"></script>
<!-- custom code ends here -->
        <title>AEM Segment | <%= xssAPI.encodeForHTML(segmentTitle) %> (boost: <%= segmentBoost %>)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <cq:includeClientLib categories="contexthub.ui.segment-editor, cq.authoring.page"/>
        <sling:include path="contexthub" resourceType="granite/contexthub/components/contexthub"/>
    </head>
    <body class="segment-structure">
        <br/><br/>
        <div class="segment-condition" data-segment-path="<%= xssAPI.encodeForHTMLAttr(segmentPath) %>">
        <%

            /* get segment traits */
            Resource segmentTraits = resourceResolver.getResource(resource, TRAITS_NODE);

            if (segmentTraits != null) {
                %><sling:include resource="<%= segmentTraits %>"/><%
            }

        %>
        </div>
        <br/><br/>
    </body>
</html>