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
    };
    footerResponse();
	
    /*Sticky Navigation*/
    var scrollTop = ($('.sticky-nav').hasClass('sticky-nav')) ? parseInt($('.sticky-nav').offset().top) : 0;
    var stickynav = function () {
        //$('.sticky-nav').height();
        $('.sticky-navbar').width($('.container-custom').outerWidth());
    };
    stickynav();
    $('.sticky-nav .dropdown-menu li > a').click(function (e) {
        var hashtag = $(this.hash),
            $target = parseInt(hashtag.offset().top) - $('.sticky-navbar').outerHeight()
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });
	
    /*Page Scroll*/
    $(window).scroll(function () {
		
		/*Sticky Nav scroll event*/
        ($(this).scrollTop() > scrollTop) ? $('.sticky-navbar').addClass('navbar-fixed-top'): $('.sticky-navbar').removeClass('navbar-fixed-top');
        $('.sticky-nav .dropdown-menu li > a').each(function () {
            var scrolltag = $(this.hash),
                $target = parseInt(scrolltag.offset().top - 1) + 45;
            if ($(window).scrollTop() >= $target) {
                $('.sticky-nav .dropdown-menu li > a').removeClass('current');
                $(this).addClass('current');
            }
        });
    });


    $('.home-header .learn-link a').click(function (e) {
        var hashtag = $(this.hash),
            $target = parseInt(hashtag.offset().top);
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });

    /*Responsive*/
    $(window).resize(function () {
        footerResponse();
        stickynav();
    });
});
