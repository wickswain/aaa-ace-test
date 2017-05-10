/**
 * Jump to Anchor with “noscript and target compare” approach.
 */
(function() {
    var hashtag = window.location.hash,
        navbarHeight = $('.navbar-fixed-top').height(),
        stickyNavbarHeight = $('.sticky-nav').height(),
        swingTime = 0;
    
    $('body').append('<input id="ajaxStopCalls" type="hidden" >');
    
    if (hashtag) {
        var targetCount = $(document).find('.target').length * 2,
            noscriptCount = $(document).find("noscript").length,
            diffCount = noscriptCount - targetCount,
            newInterval = null,
            varCount = 0;
        
        $(document).ajaxComplete(function () {
            var _nsCount = $(document).find("noscript").length;
         
            console.log("No Script count in ajax complete: " + _nsCount);
        });
        
        var checkInterval = function() {
            var hiddenInputVal = $("body").find("#ajaxStopCalls"),
            	noscriptCount = $(document).find("noscript").length;
            console.log("No Script count in Interval: " + noscriptCount);
            
            if (noscriptCount == diffCount && hiddenInputVal.length != 'undefined') {
                clearInterval(newInterval);
                jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
                $("#ajaxStopCalls").remove();
            } else {
                varCount++;
            }
        };
        
        newInterval = setInterval(checkInterval, 100);
    }
    
})();
