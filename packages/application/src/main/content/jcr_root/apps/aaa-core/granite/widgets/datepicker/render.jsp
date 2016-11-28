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
%><%@include file="/libs/granite/ui/global.jsp"%><%
%><%@page session="false"
          import="com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Field,
                  com.adobe.granite.ui.components.Tag,
                  com.day.cq.i18n.I18n,
                  org.json.JSONArray"%><%--###
DatePicker
==========

.. granite:servercomponent:: /libs/granite/ui/components/foundation/form/datepicker
   :supertype: /libs/granite/ui/components/foundation/form/field
   
   DatePicker is an input component where the user can enter date.
   
   It extends :granite:servercomponent:`Field </libs/granite/ui/components/foundation/form/field>` component.

   It has the following content structure:

   .. gnd:gnd::

      [granite:FormDatePicker]
      
      /**
       * The id attribute.
       */
      - id (String)

      /**
       * The class attribute. This is used to indicate the semantic relationship of the component similar to ``rel`` attribute.
       */
      - rel (String)

      /**
       * The class attribute.
       */
      - class (String)

      /**
       * The title attribute.
       */
      - title (String) i18n
      
      /**
       * The name that identifies the field when submitting the form.
       */
      - name (String)
      
      /**
       * The value of the field.
       */
      - value (StringEL)
      
      /**
       * A hint to the user of what can be entered in the field.
       */
      - emptyText (String) i18n
      
      /**
       * Indicates if the field is in disabled state.
       */
      - disabled (Boolean)
      
      /**
       * The type of the picker.
       */
      - type (String) = 'date' < 'date', 'datetime', 'time'
      
      /**
       * The date format to be displayed.
       */
      - displayedFormat (String) i18n
      
      /**
       * The date format for form submission.
       */
      - storedFormat = 'YYYY-MM-DD[T]HH:mm:ss.000Z'
      
      /**
       * The minimum boundary of the date.
       */
      - minDate (String)
      
      /**
       * The maximum boundary of the date.
       */
      - maxDate (String)
###--%><%

    Config cfg = cmp.getConfig();
    ValueMap vm = (ValueMap) request.getAttribute(Field.class.getName());

    String name = cfg.get("name", String.class);
    String displayFormat = i18n.getVar(cfg.get("displayedFormat", String.class));

    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();

    attrs.add("id", cfg.get("id", String.class));
    attrs.addClass(cfg.get("class", String.class));
    attrs.addRel(cfg.get("rel", String.class));
    attrs.add("title", i18n.getVar(cfg.get("title", String.class)));

    attrs.addClass("coral-InputGroup coral-DatePicker");
    attrs.add("data-init", "datepicker");
    attrs.add("value", cfg.get("value", String.class)); // FIXME illegal attribute for span
    attrs.add("data-min-date", cfg.get("minDate", String.class));
    attrs.add("data-max-date", cfg.get("maxDate", String.class));

    // FIXME data-displayed-format is a bad name; use data-display-format
    attrs.add("data-displayed-format", displayFormat);

    // Use CQ5 standard date format for storage
    // FIXME data-stored-format is a bad name; use data-value-format
    attrs.add("data-stored-format", cfg.get("storedFormat","YYYY-MM-DD[T]HH:mm:ss.000Z"));

    attrs.add("data-head-format", i18n.get("MMMM YYYY", "Datepicker headline, see moment.js for allowed formats"));

    attrs.add("data-month-names", new JSONArray(getMonthNames(i18n)).toString());
    attrs.add("data-day-names", new JSONArray(getDayNames(i18n)).toString());

    attrs.addOthers(cfg.getProperties(), "id", "class", "rel", "title", "name", "value", "emptyText", "type", "displayedFormat", "minDate", "maxDate", "fieldLabel", "fieldDescription", "renderReadOnly", "ignoreData");


    AttrBuilder attrsInput = new AttrBuilder(request, xssAPI);
    attrsInput.addClass("coral-InputGroup-input coral-Textfield");
    attrsInput.add("name", name);
    attrsInput.addDisabled(cfg.get("disabled", false));
    attrsInput.add("placeholder", i18n.getVar(cfg.get("emptyText", String.class)));
    attrsInput.add("type", cfg.get("type", "date"));
    attrsInput.add("value", vm.get("value", String.class));

%><div <%= attrs.build() %>>
    <input <%= attrsInput.build() %>>
    <span class="coral-InputGroup-button">
        <button class="coral-Button coral-Button--secondary coral-Button--square" type="button" title="<%= xssAPI.encodeForHTMLAttr(i18n.get("Date Picker")) %>">
            <i class="coral-Icon coral-Icon--sizeS coral-Icon--calendar"></i>
        </button>
    </span>
</div><%!

    private String[] getMonthNames(I18n i18n) {
        return new String[] {
                i18n.get("January"),
                i18n.get("February"),
                i18n.get("March"),
                i18n.get("April"),
                i18n.get("May"),
                i18n.get("June"),
                i18n.get("July"),
                i18n.get("August"),
                i18n.get("September"),
                i18n.get("October"),
                i18n.get("November"),
                i18n.get("December")
        };
    }

    private String[] getDayNames(I18n i18n) {
        return new String[] {
                i18n.get("Su"),
                i18n.get("Mo"),
                i18n.get("Tu"),
                i18n.get("We"),
                i18n.get("Th"),
                i18n.get("Fr"),
                i18n.get("Sa")
        };
    }
%>