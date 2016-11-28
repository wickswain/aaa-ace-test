<%--
  ADOBE CONFIDENTIAL

  Copyright 2013 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="java.util.Map,
                  java.util.HashMap,
                  org.apache.commons.collections.IteratorUtils,
                  com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.ComponentHelper,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Tag" %><%--###
Tabs
====

.. granite:servercomponent:: /libs/granite/ui/components/foundation/layouts/tabs
   :layout:
   
   The tabs layout.
   
   It has the following content structure at the layout resource:

   .. gnd:gnd::

      [granite:LayoutsTabs]
      
      /**
       * The layout mode of the tabs.
       *
       * default
       *    If the property is not specified, it is in default mode, where tab area is above the panel area.
       *    This is designed to divide pieces of content within a page.
       * stacked
       *    The tab area is side-by-side with the panel area.
       *    This is designed to divide pieces of content within a page.
       * large
       *    Similar to default mode, but larger. This is designed to divide pages.
       */
      - type (String) < 'stacked', 'large'
      
      /**
       * ``true`` to add padding to each panel; ``false`` otherwise.
       */
      - padding (Boolean) = 'true'
      
   Each tab can be any component, and specified using :ref:`ItemDataSource <ItemDataSource>`. It also needs at very least satisfies the following content structure:
   
   .. gnd:gnd::

      [granite:LayoutsTabsTab]
      
      /**
       * The tab title.
       */
      - jcr:title (String) mandatory i18n
      
      /**
       * The item specific config for the layout.
       */
      + layoutConfig (granite:LayoutsTabsTabLayoutconfig)
      
      
      [granite:LayoutsTabsTabLayoutconfig]
      
      /**
       * Indicates if the tab is active. If no single tab's ``active`` property is specifically set to ``true``, then the first tab is active.
       */
      - active (Boolean)
      
      /**
       * Indicates the icon of the tab.
       */
      - icon (String)
      
      /**
       * ``true`` to add padding for the panel. This property only makes sense when ``padding`` property at the layout node is set to ``false``.
       */
      - padding (Boolean)

   Example::
   
      + mytabs
        - sling:resourceType = "granite/ui/components/foundation/container"
        + layout
          - sling:resourceType = "granite/ui/components/foundation/layouts/tabs"
        + items
          + tab1
            - jcr:title = "Tab 1"
            - sling:resourceType = "granite/ui/components/foundation/container"
          + tab2
            - jcr:title = "Tab 2"
            - sling:resourceType = "granite/ui/components/foundation/container"
            + layoutConfig
              - active = true
###--%><%

    Config layout = new Config(resource.getChild(Config.LAYOUT));

    Tag tag = cmp.consumeTag();

    AttrBuilder attrs = tag.getAttrs();
    attrs.addClass("coral-TabPanel");
    attrs.addClass(getTypeClass(layout.get("type", String.class)));
    
    if (!layout.get("padding", true)) {
        attrs.addClass("coral-TabPanel--graniteNoPadding");
    }

    attrs.add("data-init", "tabs");

    Resource[] items = (Resource[]) IteratorUtils.toArray(cmp.getItemDataSource().iterator(), Resource.class);
    boolean hasActive = hasActive(items);
    Map<String, Boolean> renderConditions = new HashMap<String, Boolean>();
    Map<String, Config> inclConfigs = new HashMap<String, Config>();

tag.printlnStart(out);
	%><nav class="coral-TabPanel-navigation"><%
	    for (int i = 0; i < items.length; i++) {
	        Resource item = items[i];
            Config itemCfg = new Config(item);

            // item which holds the render conditions
            Resource conditionResource = item;

            // config of tabs included by include component
            Config inclCfg = null;
            if (item.isResourceType("granite/ui/components/foundation/include")) {
                String path = itemCfg.get("path", String.class);
                if (path != null) {
                    // use resourceResolver in order to apply the search path
                    Resource inclResource = resourceResolver.getResource(path);
                    if (inclResource != null) {
                        inclCfg = new Config(inclResource);
                        inclConfigs.put(item.getName(), inclCfg);
                        if (item.getChild("granite:rendercondition") == null ||
                                item.getChild("rendercondition") == null) {
                            // item itself does not define render conditions; use included resource
                            conditionResource = inclResource;
                        }
                    }
                }
            }

            if (!cmp.getRenderCondition(conditionResource).check()) {
                renderConditions.put(item.getName(), false);
                continue;
            }

	        String title = i18n.getVar(itemCfg.get("jcr:title", inclCfg == null ? "" : inclCfg.get("jcr:title", "")));
 	        String className = i18n.getVar(itemCfg.get("class", inclCfg == null ? "" : inclCfg.get("class", "")));
			String targetValue = i18n.getVar(itemCfg.get("showhidetargetvalue", inclCfg == null ? "" : inclCfg.get("showhidetargetvalue", "")));

	        AttrBuilder itemAttrs = new AttrBuilder(request, xssAPI);
	        itemAttrs.addClass("coral-TabPanel-tab " + className);
            itemAttrs.addRel(itemCfg.get("granite:rel", inclCfg == null ? "" : inclCfg.get("granite:rel", "")));
            itemAttrs.addClass(itemCfg.get("layoutConfig/class", inclCfg == null ? "" : inclCfg.get("layoutConfig/class", "")));
	        itemAttrs.addHref("href", "#");
	        itemAttrs.add("data-toggle", "tab");

            if(className != "") {
				itemAttrs.add("data-showhidetargetvalue", targetValue);
            }
	
	        if ((!hasActive && i == 0) || isActive(itemCfg)) {
	            itemAttrs.addClass("is-active");
	        }

            String icon = getIcon(itemCfg, inclCfg, cmp);
	        if (!"".equals(icon)) {
                itemAttrs.addClass("coral-TabPanel-tab--icon");
                itemAttrs.add("title", title);

	            AttrBuilder iconAttrs = new AttrBuilder(request, xssAPI);
	            iconAttrs.addClass(icon);
	
	            %><a <%= itemAttrs.build() %>><i <%= iconAttrs.build() %>></i></a><%
	        } else {
	            %><a <%= itemAttrs.build() %>><%= outVar(xssAPI, i18n, title) %></a><%
	        }
	    }
	%></nav>
	<div class="coral-TabPanel-content"><%
	    for (int i = 0; i < items.length; i++) {
	        Resource item = items[i];
            if (renderConditions.get(item.getName()) != null) {
                continue;
            }

            Config inclCfg = inclConfigs.get(item.getName());

	        Config itemCfg = new Config(item);
	        AttrBuilder itemAttrs = new AttrBuilder(request, xssAPI);
	        itemAttrs.addClass("coral-TabPanel-pane");
	        itemAttrs.addClass(itemCfg.get("layoutConfig/class", inclCfg == null ? "" : inclCfg.get("layoutConfig/class", "")));
	        
	        if (itemCfg.get("layoutConfig/padding", inclCfg == null ? false : inclCfg.get("layoutConfig/padding", false))) {
	            itemAttrs.addClass("coral-TabPanel-pane--granitePadding");
	        }
	        
	        if ((!hasActive && i == 0) || isActive(itemCfg)) {
	            itemAttrs.addClass("is-active");
	        }

            cmp.include(item, new Tag(itemAttrs));
	    }
	%></div><%
tag.printlnEnd(out);
%><%!
    private boolean hasActive(Resource[] items) {
        for (Resource item : items) {
            if (isActive(new Config(item))) {
                return true;
            }
        }

        return false;
    }

    private String getTypeClass(String type) {
        if ("nav".equals(type)) {
            type = "large";
        }
        
        if (type == null) {
            return null;
        }
        
        return "coral-TabPanel--" + type;
    }

    private boolean isActive(Config itemCfg) {
        // active is deprecated, please use layoutConfig/active
        return itemCfg.get("layoutConfig/active", itemCfg.get("active", false));
    }

    private String getIcon(Config itemCfg, Config inclCfg, ComponentHelper cmp) {
        String icon = itemCfg.get("layoutConfig/icon", inclCfg == null ? "" : inclCfg.get("layoutConfig/icon"));
        if (!"".equals(icon)) {
            icon = "coral-Icon " + cmp.getIconClass(icon);
        }
        return icon;
    }
%>

