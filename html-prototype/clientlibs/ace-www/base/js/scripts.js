/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:17-August-2016
    Version:0.1
*****************************************/
$(function () {
    $('.sticky-nav .dropdown-menu a').click(function (e) {
        var target = $(this.hash);
        if (location.hostname == this.hostname) {
            if (target.length != 0) {
                $('html,body').animate({
                    scrollTop: target.offset().top
                }, 1000);
                return false;
            }
        }
        e.preventDefault();
    });
    $(window).resize(function () {
        footerResponse();
        $('.sticky-nav').attr({
            'data-offset-top': $('.sticky-nav').offset().top
        }).width($('.container-custom').width());
    });
    footerResponse = function () {
        if ($(window).width() <= 992) {
            $('.list-header').each(function () {
                $(this).next().removeClass('in');
                $('.collapse').on('shown.bs.collapse', function () {
                    $(this).parent().find(".glyphicon-chevron-down").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
                }).on('hidden.bs.collapse', function () {
                    $(this).parent().find(".glyphicon-chevron-up").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
                });
            });
        } else {
            $('.list-header').parent().find('ul').addClass('in').removeAttr('style');
        }
    }
    footerResponse();
	$('.sticky-nav').attr({
		'data-offset-top': $('.sticky-nav').offset().top
	}).width($('.container-custom').width());

});
