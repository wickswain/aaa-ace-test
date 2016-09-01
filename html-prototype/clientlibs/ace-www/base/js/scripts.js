/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:01-September-2016
    Version:0.3
*****************************************/
$(function () {
	
    /*Footer Responsive script*/
    var footerResponse = function () {
        if ($(window).width() <= 992) {
            $('.list-header').each(function () {
                $(this).next().removeClass('in');

            });
        } else {
            $('.list-header').parent().find('ul').addClass('in').removeAttr('style');
        }
    };footerResponse();
	
    /*Sticky Navigation*/
    var scrollTop = parseInt($('.sticky-nav').offset().top);
    var stickynav = function () {
        $('.sticky-nav').width($('.container-custom').width());
    };stickynav();
	
    $('.sticky-nav .dropdown-menu li > a').click(function (e) {
        var hashtag = $(this.hash),
            $target = parseInt(hashtag.offset().top) - $('.sticky-nav').height()
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });
	
    /*Page Scroll*/
    $(window).scroll(function () {
        ($(this).scrollTop() > scrollTop) ? $('.sticky-nav').addClass('navbar-fixed-top'): $('.sticky-nav').removeClass('navbar-fixed-top');
        $('.sticky-nav .dropdown-menu li > a').each(function () {
            var scrolltag = $(this.hash),
                $target = parseInt(scrolltag.offset().top) + 45;
            if ($(window).scrollTop() >= $target) {
                $('.sticky-nav .dropdown-menu li > a').removeClass('current');
                $(this).addClass('current');
            }
        });
    });
    /*Responsive*/
    $(window).resize(function () {
        footerResponse();
        stickynav();
    });
});
