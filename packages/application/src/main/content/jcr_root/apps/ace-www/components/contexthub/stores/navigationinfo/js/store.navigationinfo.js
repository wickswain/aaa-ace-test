/*
 *  Navigation Information ContextHub Store
 *  ---------------------------------------
 * 
 *  This store holds the navigation information.
 *
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
				'[loading] contexthub.store.contexthub.navigationinfo - store.navigationinfo.js');

(function($) {
	'use strict';

	/**
	 * Gets navigation information.
	 * 
     * @return
	 */
	var getNavigationInfo = function() {
		var activeNavLink = readCookie('activenavigationlink');

		return {
			navigationinfo : {
				activelink : activeNavLink
			}
		};
	};


    /**
	 * Gets cookie value.
	 * 
     * @param cookie name
     * @return requestInfo object.
	 */
	function readCookie(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');

        for(var i=0;i < ca.length;i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') {
                c = c.substring(1,c.length);
                if (c.indexOf(nameEQ) == 0) { 
                    return c.substring(nameEQ.length,c.length);
                }
            }
        }

        return null;
    }

	/**
	 * Navigation Information store.
	 * 
	 * @constructor
	 */
	function NavigationInfoStore(name, config) {
        this.init(name, config);

		/**
		 * Navigation Information store
		 * 
		 * @param {Boolean}
		 *            keepRemainingData
		 */
		this.reset = function(keepRemainingData) {
			this.uber('reset', keepRemainingData);
			this.update();
		};

		/**
		 * Update Navigation Information
		 * 
		 */
		this.update = function() {
			this.updateNavigationInfo();
		};

		/**
		 * Update the navigation information
		 * 
		 */
		this.updateNavigationInfo = function() {
			var navInfo = getNavigationInfo();
            this.setItem('navigationinfo', navInfo.navigationinfo);
		};

		// init
		this.update();
    }

	/* Inherit from ContextHub.Store.PersistedStore */
	ContextHub.Utils.inheritance.inherit(NavigationInfoStore,
			ContextHub.Store.PersistedStore);

	/* Register Store */
	ContextHub.Utils.storeCandidates.registerStoreCandidate(
			NavigationInfoStore, 'contexthub.navigationinfo', 0);

}(ContextHubJQ));
