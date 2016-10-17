/*
 *  Tealium Information ContextHub Store
 *  ---------------------------------------
 * 
 *  This store holds the Tealium data object information.
 *
 */

ContextHub.console.log(ContextHub.Shared.timestamp(),
				'[loading] contexthub.store.contexthub.tealiuminfo - store.tealiuminfo.js');

(function($) {
	'use strict';

	/**
	 * Gets Tealium data object information.
	 * 
     * @return tealium data object.
	 */
	var getTealiumInfo = function() {
		var isAutoInsurance = false;
        var isIDTheftLinkClicked = false;

        if(typeof aceDataObject != undefined) {
            if(aceDataObject.acei01 != null) {
				isAutoInsurance = aceDataObject.acei01;
            }

            if(aceDataObject.acef01 != null) {
				isIDTheftLinkClicked = aceDataObject.acef01;
            }
            
            console.log("isAutoInsurance: " + isAutoInsurance);
            console.log("isIDTheftLinkClicked: " + isIDTheftLinkClicked);
        }
        
		return {
			tealiuminfo : {
				isAutoInsurance : isAutoInsurance,
                isIDTheftLinkClicked : isIDTheftLinkClicked
			}
		};
	};

	/**
	 * Tealium Information store.
	 * 
	 * @constructor
	 */
	function TealiumInfoStore(name, config) {
        this.init(name, config);

		/**
		 * Tealium Information store
		 * 
		 * @param {Boolean}
		 *            keepRemainingData
		 */
		this.reset = function(keepRemainingData) {
			this.uber('reset', keepRemainingData);
			this.update();
		};

		/**
		 * Update Tealium Information
		 * 
		 */
		this.update = function() {
			this.updateTealiumInfo();
		};

		/**
		 * Update the tealium information
		 * 
		 */
		this.updateTealiumInfo = function() {
			var tealiumInfo = getTealiumInfo();
            this.setItem('tealiuminfo', tealiumInfo.tealiuminfo);
		};

		// init
		this.update();
    }

	/* Inherit from ContextHub.Store.PersistedStore */
	ContextHub.Utils.inheritance.inherit(TealiumInfoStore,
			ContextHub.Store.PersistedStore);

	/* Register Store */
	ContextHub.Utils.storeCandidates.registerStoreCandidate(
			TealiumInfoStore, 'contexthub.tealiuminfo', 0);

}(ContextHubJQ));
