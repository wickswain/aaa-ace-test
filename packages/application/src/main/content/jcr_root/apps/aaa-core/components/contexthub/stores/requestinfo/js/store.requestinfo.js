/*
 *  Request Information ContextHub Store
 *  ---------------------------------------
 * 
 *  This store holds the request parameters information like host name and etc.
 *
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
				'[loading] contexthub.store.contexthub.requestinfo - store.requestinfo.js');

(function($) {
	'use strict';

	/**
	 * Gets request information.
	 * 
     * @return requestInfo object.
	 */
	var getRequestInfo = function() {
		var hostName = location.hostname;
		var regionName = '';

		// Extract the region information from host name resolved from request.
		if (hostName.split(".").length >= 2) {
			regionName = hostName.split(".")[1];
		}

		return {
			requestinfo : {
				hostname : hostName,
				region : regionName
			}
		};
	};

	/**
	 * Request Information store.
	 * 
	 * @constructor
	 */
	function RequestInfoStore(name, config) {
		this.init(name, config);

		/**
		 * Request Information store
		 * 
		 * @param {Boolean}
		 *            keepRemainingData
		 */
		this.reset = function(keepRemainingData) {
			this.uber('reset', keepRemainingData);
			this.update();
		};

		/**
		 * Update Request Info
		 * 
		 */
		this.update = function() {
			this.updateRequestInfo();
		};

		/**
		 * Update the request info
		 * 
		 */
		this.updateRequestInfo = function() {
			var requestInfo = getRequestInfo();
            this.setItem('requestinfo', requestInfo.requestinfo);
		};

		// init
		this.update();
	}

	/* Inherit from ContextHub.Store.PersistedStore */
	ContextHub.Utils.inheritance.inherit(RequestInfoStore,
			ContextHub.Store.PersistedStore);

	/* Register Store */
	ContextHub.Utils.storeCandidates.registerStoreCandidate(
			RequestInfoStore, 'contexthub.requestinfo', 0);

}(ContextHubJQ));
