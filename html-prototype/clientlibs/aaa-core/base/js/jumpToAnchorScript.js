/**
 * Jump to Anchor with “finding Ajax components” approach.
 */
(function() {
    var numAjaxComp = 1; //Due to signUp ajaxCall
    var ajaxStopCalls = 0;
    $('body').append('<input id="ajaxStopCalls" data-id="' + ajaxStopCalls + '" type="hidden" >'); //Appending hidden element to page
    var hashtag = window.location.hash,
        navbarHeight = $('.navbar-fixed-top').height(),
        stickyNavbarHeight = $('.sticky-nav').height(),
        swingTime = 0;
    $(document).ajaxStop(function() { //ajaxStop method strart
        var ajaxComponetCount = $('.ajaxComponent').length,
            numAjaxCompCount,
            jumpInterval = null,
            varCounter = 0;
        if (ajaxComponetCount == 0) {
            numAjaxCompCount = numAjaxComp + 1;
        } else {
            numAjaxCompCount = numAjaxComp + ajaxComponetCount;
        }
        ajaxStopCalls++;
        $("body").find("#ajaxStopCalls").attr('data-id', ajaxStopCalls); //Appending current ajaxStopCalls 
        if (ajaxStopCalls == numAjaxCompCount) { //Check the no of ajaxStopCalls and Ajax Components
            var hiddenInputVal = $("body").find("#ajaxStopCalls").attr('data-id');
            var jumpFun = function() {
                if (hiddenInputVal < varCounter) {
                    clearInterval(jumpInterval);
                    jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
                    $("#ajaxStopCalls").remove();
                } else {
                    varCounter++;
                    var hiddenInput = $("#ajaxStopCalls").length;
                    if (hiddenInput == 0) {
                        clearInterval(jumpInterval);
                    }
                }
            };
            jumpInterval = setInterval(jumpFun, 1);
        }
    });
})();