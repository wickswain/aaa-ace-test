<%@page session="false"%><%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.


  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.


  ==============================================================================


  Default reference component.


  Includes the referenced component addressed by the "path" property. It
  temporarily disables the WCM so that the included components cannot be
  edited.


  ==============================================================================


--%><%@page import="com.day.cq.wcm.api.WCMMode,
    com.day.cq.wcm.api.components.DropTarget, com.day.cq.wcm.foundation.Placeholder,
    com.aaa.ace.services.PersonalizationConfigService,
    com.aaa.ace.services.RegionDataService,
    org.apache.sling.api.resource.ResourceUtil" %><%
  %><%@include file="/libs/foundation/global.jsp" %><%


WCMMode mode = WCMMode.DISABLED.toRequest(request);

// Remember the mode on the original page before any reference started
String originalModeKey = "com.day.cq.wcm.components.reference.mode";
WCMMode originalMode = (WCMMode)request.getAttribute(originalModeKey);
if (originalMode == null) {
    originalMode = mode;
    request.setAttribute(originalModeKey, originalMode);
}


boolean needToCloseDiv = false;
String showAltComponentName = request.getParameter("showAltComponentName");
try {
    // Use request attributes to guard against reference loops
    String path = resource.getPath();
    String key = "com.day.cq.wcm.components.reference:" + path;
    if (request.getAttribute(key) == null) {
        request.setAttribute(key, Boolean.TRUE);
    } else {
        throw new IllegalStateException("Reference loop: " + path);
    }

    //drop target css class = dd prefix + name of the drop target in the edit config
    String ddClassName = DropTarget.CSS_CLASS_PREFIX + "paragraph";

    // Include the target paragraph by path
    String targetPath = properties.get("path", String.class);

    RegionDataService regionService = sling.getService(RegionDataService.class);
    String regionName = regionService.getRegionInfo(slingRequest);

    if (regionName != null) {

        PersonalizationConfigService configService = sling.getService(PersonalizationConfigService.class);
        String target = configService.getFormattedPersonalizationPagePath(slingRequest, targetPath);

        if (target != null) {
            if (!ResourceUtil.isNonExistingResource(resourceResolver.resolve(target))) {
                %><div class="<%= ddClassName %>"><% needToCloseDiv = true; %><sling:include path="<%= target %>"/></div><% needToCloseDiv = false; %><%
            } else {
                if (mode == WCMMode.EDIT) {
                    String placeholder = "Region name resolved to '" + regionName + "' but the reference component is not authored. Please author the component.";
                    %><h2 style="text-align: center;" class="cq-paragraphreference-thumbnail-text"><%= placeholder %></h2><%
                }
            }
        } else {
           if (mode == WCMMode.EDIT) {
               String classicPlaceholder =
                    "<p><img src=\"/libs/cq/ui/resources/0.gif\" class=\"cq-reference-placeholder " + ddClassName + "\" alt=\"\"></p>";
               String placeholder = Placeholder.getDefaultPlaceholder(slingRequest, component, classicPlaceholder, ddClassName);
               %><%= placeholder %><%
           } else {
                if(showAltComponentName!=null && showAltComponentName.equalsIgnoreCase("true")) { %>
                    <h2 class="cq-paragraphreference-thumbnail-text"><%= resource.getName() %></h2>
                <%}
           }
        }
    } else {
        if (mode == WCMMode.EDIT) {
            String placeholder = "Region Name is not resolved.";
            %><h2 style="text-align: center;" class="cq-paragraphreference-thumbnail-text"><%= placeholder %></h2><%
        }
    }

} catch (Exception e) {
    // Log errors always
    log.error("Reference component error", e);
    // Display errors only in edit mode
    if (originalMode == WCMMode.EDIT) {
        %><p>Reference error: <%= xssAPI.encodeForHTML(e.toString()) %></p><%
    } else {
        if(showAltComponentName!=null && showAltComponentName.equalsIgnoreCase("true")) { %>
            <h2 class="cq-paragraphreference-thumbnail-text"><%= resource.getName() %></h2>
        <%}
    }

} finally {
    if (needToCloseDiv) {
        %></div><%
    }
    mode.toRequest(request);
}
%>
