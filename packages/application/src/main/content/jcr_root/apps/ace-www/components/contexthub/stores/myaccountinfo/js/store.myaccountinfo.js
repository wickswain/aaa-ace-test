/*
 *  MyAccount Information ContextHub Store
 *  ---------------------------------------
 * 
 *  This store holds the logged in user account information.
 *
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
				'[loading] contexthub.store.contexthub.myaccountinfo - store.myaccountinfo.js');

(function($) {
	'use strict';

	/**
	 * Gets logged in user account information.
	 * 
     * @return requestInfo object.
	 */
	var getMyAccountInfo = function() {
		var isLoggedIn = true;
        
		return {
			myaccountinfo : {
				isloggedin : isLoggedIn
			}
		};
	};

	/**
	 * MyAccount Information store.
	 * 
	 * @constructor
	 */
	function MyAccountInfoStore(name, config) {
        this.init(name, config);

		/**
		 * MyAccount Information store
		 * 
		 * @param {Boolean}
		 *            keepRemainingData
		 */
		this.reset = function(keepRemainingData) {
			this.uber('reset', keepRemainingData);
			this.update();
		};

		/**
		 * Update MyAccount Information
		 * 
		 */
		this.update = function() {
			this.updateMyAccountInfo();
		};

		/**
		 * Update the myaccount information
		 * 
		 */
		this.updateMyAccountInfo = function() {
			var accountInfo = getMyAccountInfo();
            this.setItem('myaccountinfo', accountInfo.myaccountinfo);
		};

		// init
		this.update();
    }

	/* Inherit from ContextHub.Store.PersistedStore */
	ContextHub.Utils.inheritance.inherit(MyAccountInfoStore,
			ContextHub.Store.PersistedStore);

	/* Register Store */
	ContextHub.Utils.storeCandidates.registerStoreCandidate(
			MyAccountInfoStore, 'contexthub.myaccountinfo', 0);

}(ContextHubJQ));
