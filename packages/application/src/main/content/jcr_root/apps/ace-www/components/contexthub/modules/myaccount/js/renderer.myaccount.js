/*
 *  MyAccount ContextHub UI Module
 *  --------------------------------------------
 * 
 *  This ui module displays the region information on ContextHub.
 *  MyAccount information will be resolved through cookie received after the login.  
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
		'[loading] contexthub.module.contexthub.myaccount - renderer.myaccount.js');

(function() {
	'use strict';

	/**
	 * MyAccount information module.
	 * 
	 * @constructor
	 */
	var MyAccountInfoRenderer = function() {
	};

	/* Inherit from ContextHub.UI.BaseModuleRenderer */
	ContextHub.Utils.inheritance.inherit(MyAccountInfoRenderer,
			ContextHub.UI.BaseModuleRenderer);

	/* Default configuration */
	MyAccountInfoRenderer.prototype.defaultConfig = {
		icon : 'coral-Icon--login',
		title : 'MyAccount Details',
		clickable: true,
		editable: {
            key: '/myaccountinfo',

            /* list of disabled properties */
            disabled: [
            ],

            /* list of hidden properties */
            hidden: [
            ]
        },
        storeMapping : {
			accountInfo : 'myaccountinfo'
		},

		template : '<p>{{i18n "My Account Details"}}</p>'
				+ '<p>{{i18n "Is Logged-In: "}}{{accountInfo.myaccountinfo.isloggedin}}</p>'
	};

    /* Popover Content Configuration */
    MyAccountInfoRenderer.prototype.getPopoverContent = function(module, popoverVariant) {
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
	ContextHub.UI.ModuleRenderer('contexthub.myaccount', new MyAccountInfoRenderer());

}());
