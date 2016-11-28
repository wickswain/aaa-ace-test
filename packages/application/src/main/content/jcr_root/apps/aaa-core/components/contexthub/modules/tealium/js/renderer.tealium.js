/*
 *  Tealium ContextHub UI Module
 *  --------------------------------------------
 * 
 *  This UI module displays the tealium data information on ContextHub.
 *  Tealium Data information will be resolved through java script object received from Tealium server.  
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
		'[loading] contexthub.module.contexthub.tealium - renderer.tealium.js');

(function() {
	'use strict';

	/**
	 * Tealium information module.
	 * 
	 * @constructor
	 */
	var TealiumInfoRenderer = function() {
	};

	/* Inherit from ContextHub.UI.BaseModuleRenderer */
	ContextHub.Utils.inheritance.inherit(TealiumInfoRenderer,
			ContextHub.UI.BaseModuleRenderer);

	/* Default configuration */
	TealiumInfoRenderer.prototype.defaultConfig = {
		icon : 'coral-Icon--info',
		title : 'Tealium Details',
		clickable: true,
		editable: {
            key: '/tealiuminfo',

            /* list of disabled properties */
            disabled: [
            ],

            /* list of hidden properties */
            hidden: [
            ]
        },
        storeMapping : {
			tealiumInfo : 'tealiuminfo'
		},

		template : '<p>{{i18n "Tealium Details"}}</p>'
				+ '<p>{{i18n "Is Auto Insurance: "}}{{tealiumInfo.tealiuminfo.isAutoInsurance}}</p>'
        		+ '<p>{{i18n "Is IDTheft Link Clicked: "}}{{tealiumInfo.tealiuminfo.isIDTheftLinkClicked}}</p>'
	};

    /* Popover Content Configuration */
    TealiumInfoRenderer.prototype.getPopoverContent = function(module, popoverVariant) {
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
	ContextHub.UI.ModuleRenderer('contexthub.tealium', new TealiumInfoRenderer());

}());
