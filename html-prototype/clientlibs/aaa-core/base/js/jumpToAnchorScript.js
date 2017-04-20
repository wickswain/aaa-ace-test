(function() {
    var numAjaxComp = 1; //Due to signUp ajaxCall
    var ajaxStopCalls = 0;
    //Appending hidden element to page
    $('body').append('<input id="ajaxStopCalls" data-id="' + ajaxStopCalls + '" type="hidden" >');

    //Default Jump methord need ot execute when ther in ajaxCalls
    var hashtag = window.location.hash,
        navbarHeight = $('.navbar-fixed-top').height(),
        stickyNavbarHeight = $('.sticky-nav').height(),
        swingTime = 0;
    jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
    //Default Jump methord end

    //ajaxStop method strart
    $(document).ajaxStop(function() {
        var ajaxComponetCount = $('.ajaxComponent').length;
        var numAjaxCompCount = numAjaxComp + ajaxComponetCount;
        var jumpInterval = null;
        var varCounter = 0;

        console.log('ajaxComponent-length: ' + $('.ajaxComponent').length);
        console.log('numAjaxComp: ' + numAjaxCompCount);
        ajaxStopCalls++;
        console.log("ajaxStopCalls: " + ajaxStopCalls);

        //Appending current ajaxStopCalls 
        $("body").find("#ajaxStopCalls").attr('data-id', ajaxStopCalls);

        //Check the no of ajaxStopCalls and Ajax Components
        if (ajaxStopCalls == numAjaxCompCount) {

            var hiddenInputVal = $("body").find("#ajaxStopCalls").attr('data-id');
            var jumpFun = function() {

                if (hiddenInputVal == varCounter) {
                    clearInterval(jumpInterval);
                    console.log('Jump');
                    jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
                } else {
                    varCounter++;
                    console.log('Interval');
                    var hiddenInput = $("#ajaxStopCalls").length;
                    if (hiddenInput == 0) {
                        clearInterval(jumpInterval);
                        console.log(hiddenInput);
                    }
                }
            };
            jumpInterval = setInterval(jumpFun, 1);
        }
    });
    console.log('numAjaxComp: ' + numAjaxComp);
    //ajaxStop method strart
})();