/**
 * Jump to Anchor with “noscript and target compare” approach.
 */
(function() {
    var _targetCount = $(document).find('.target').length * 2,
    	hashtag = window.location.hash;

    if (hashtag) {
        var _nsCount = $(document).find("noscript").length,
            diffCount = _nsCount - _targetCount,
            navbarHeight = $('.navbar-fixed-top').height(),
            stickyNavbarHeight = $('.sticky-nav').height(),
            swingTime = 0;

        if ( _targetCount > 0 ) {
	        $(document).ajaxComplete(function () {
	            var _nsCount = $(document).find("noscript").length;
	            
	            if (_nsCount <= diffCount) {
					jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
	            }
	        });
        } else {
        	jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
        }
    }
    
})();
