/*****************************************
    Template : AAA Scripts
    Created Date:21-December-2016
    Modified Date:21-December-2016
    Version:4
*****************************************/
$j(function ($) {
    /*Footer Responsive script*/
    function footerResponse() {
        if ($(window).width() <= 992) {
            $('.list-header').each(function () {
                $(this).next().removeClass('in');
            });
        }
        else {
            $('.list-header').parent().find('ul').addClass('in').removeAttr('style');
        }
    }
    footerResponse();
    /*Sticky Navigation*/
    /*Home Header resize*/
    function headerResize() {}
    /* Nav Content handling */
    /*Page Scroll*/
    /*Responsive*/
    $(window).resize(function () {
        headerResize();
        footerResponse();
    });
    /*Navigation*/
    /* Creates and store the cookie */
    // Navigation Close event /
});