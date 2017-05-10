/**
 * Jump to Anchor with “noscript and target compere” approach.
 */
(function() {
    $('body').append('<input id="ajaxStopCalls" type="hidden" >');
    var hashtag = window.location.hash,
        navbarHeight = $('.navbar-fixed-top').height(),
        stickyNavbarHeight = $('.sticky-nav').height(),
        swingTime = 0;
    if (hashtag) {
        var targetCount = $(document).find('.target').length * 2,
            noscriptCount = $(document).find("noscript").length,
            diffCount = noscriptCount - targetCount,
            newInterval = null,
            varCount = 0;
        var checkInterval = function() {
            var hiddenInputVal = $("body").find("#ajaxStopCalls");
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