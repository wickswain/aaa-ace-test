/*
 *  Region ContextHub UI Module
 *  --------------------------------------------
 * 
 *  This ui module displays the region information on ContextHub.
 *  Region information will be resolved through host name received in the request.
 *
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
		'[loading] contexthub.module.contexthub.region - renderer.region.js');

(function() {
	'use strict';

	/**
	 * Region information module.
	 *
	 * @constructor
	 */
	var RegionInfoRenderer = function() {
	};

	/* Inherit from ContextHub.UI.BaseModuleRenderer */
	ContextHub.Utils.inheritance.inherit(RegionInfoRenderer,
			ContextHub.UI.BaseModuleRenderer);

	/* Default configuration */
	RegionInfoRenderer.prototype.defaultConfig = {
		icon : 'coral-Icon--location',
		title : 'Region Details',
		clickable: true,
		editable: {
            key: '/requestinfo',

            /* list of disabled properties */
            disabled: [
				'/requestinfo/requestinfo/hostname'
            ],

            /* list of hidden properties */
            hidden: [
            ]
        },
        storeMapping : {
			regionInfo : 'requestinfo'
		},

		template : '<p>{{i18n "Region Details"}}</p>'
				+ '<p>{{i18n "Region Name: "}}{{regionInfo.requestinfo.region}}</p>'
	};

    /* Popover Content Configuration */
    RegionInfoRenderer.prototype.getPopoverContent = function(module, popoverVariant) {
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
	ContextHub.UI.ModuleRenderer('contexthub.region', new RegionInfoRenderer());

}());
