/*
 *  Navigation ContextHub UI Module
 *  --------------------------------------------
 * 
 *  This ui module displays the navigation information on ContextHub.
 * 
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
		'[loading] contexthub.module.contexthub.navigation - renderer.navigation.js');

(function() {
	'use strict';

	/**
	 * Navigation information module.
	 * 
	 * @constructor
	 */
	var NavigationInfoRenderer = function() {
	};

	/* Inherit from ContextHub.UI.BaseModuleRenderer */
	ContextHub.Utils.inheritance.inherit(NavigationInfoRenderer,
			ContextHub.UI.BaseModuleRenderer);

	/* Default configuration */
	NavigationInfoRenderer.prototype.defaultConfig = {
		icon : 'coral-Icon--user',
		title : 'Navigation Details',
		clickable: true,
		editable: {
            key: '/navigationinfo',

            /* list of disabled properties */
            disabled: [
            ],

            /* list of hidden properties */
            hidden: [
            ]
        },
        storeMapping : {
			navInfo : 'navigationinfo'
		},

		template : '<p>{{i18n "Navigation Details"}}</p>'
				+ '<p>{{i18n "Active Navigation Link: "}}{{navInfo.navigationinfo.activelink}}</p>'
	};

    /* Popover Content Configuration */
    NavigationInfoRenderer.prototype.getPopoverContent = function(module, popoverVariant) {
        var config = $.extend(true, {}, this.defaultConfig, module.config);

        /* edition mode */
        if (popoverVariant === 'module-editing') {
            var list = this.prepareGenericList(config.editable.key, config);

            config.listType = 'input';
            config.list = list;
        }

        module.config = config;

        return this.uber('getPopoverContent', module);
    };

	/* Register module */
	ContextHub.UI.ModuleRenderer('contexthub.navigation', new NavigationInfoRenderer());

}());
