/**
 * Jump to Anchor with “noscript and target compere” approach.
 */
(function() {
	$('body').append('<input id="ajaxStopCalls" data-id="' + ajaxStopCalls + '" type="hidden" >'); //Appending hidden element to page
		var hashtag = window.location.hash,
        navbarHeight = $('.navbar-fixed-top').height(),
        stickyNavbarHeight = $('.sticky-nav').height(),
        swingTime = 0;

		var targetCount = $(document).find('.target').length * 2,
    	noscriptCount = $(document).find("noscript").length,
    	diffCount = noscriptCount - targetCount,
    	newInterval = null,
    	varCount = 0;
    
		var checkInterval = function(){
            var hiddenInputVal = $("body").find("#ajaxStopCalls");
            if(noscriptCount == diffCount && hiddenInputVal.length != 'undefined'){
                clearInterval(newInterval);
                jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
                $("#ajaxStopCalls").remove();
                console.log('On Jump  targetCount : ' + targetCount + ' noscriptCount : ' + noscriptCount);
            }
            else {
                varCount++;
            }
            console.log('Method Enter');
    };
    newInterval = setInterval(checkInterval, 100);
    console.log('On Page Load targetCount: ' + targetCount + ' noscriptCount: ' + noscriptCount);
})();