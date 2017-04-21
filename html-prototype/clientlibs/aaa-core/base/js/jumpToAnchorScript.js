/**
 * Jump to Anchor with “noscript” approach.
 */
(function() {
    $('body').append('<input id="ajaxStopCalls" type="hidden" >'); //Appending hidden element to page
    $(document).ajaxComplete(function() {
        var _ns = $(document).find("noscript");
        var hashtag = window.location.hash,
            jumpInterval = null,
            navbarHeight = $('.navbar-fixed-top').height(),
            stickyNavbarHeight = $('.sticky-nav').height(),
            swingTime = 0;
        var jumpFun = function() {
            _ns = $(document).find("noscript");
            console.log('_ns count: ' + _ns.length);
            if (_ns.length <= 1 && $("#ajaxStopCalls").length != undefined) {
                jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
                $("#ajaxStopCalls").remove(); //Removed hidden element from page
                clearInterval(jumpInterval);
            }
        };
        jumpInterval = setInterval(jumpFun, 1);
    });
})();