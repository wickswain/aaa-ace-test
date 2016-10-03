/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:30-September-2016
    Version:2.2
*****************************************/
$j(function ($) {

    /*Navigation events*/
    $('.navigation-bar .nav > li.dropdown:not(:last-child) > a').click(function (e) {
        var navid = $(this).attr('id');

        if ($(window).width() > 992) {
            if ($(this).hasClass('opend')) {
                $('.navigation-bar .nav > li:not(:last-child) > a').removeClass('opend deactive').next().stop().fadeOut();
                $(".overlay").stop().fadeOut();
            } else {
                $('.navigation-bar .nav > li:not(:last-child) > a').not($(this)).removeClass('opend').addClass('deactive').next().stop().fadeOut();
                $(this).addClass('opend').removeClass('deactive').next().stop().fadeIn();
                $(".overlay").stop().fadeIn();
            }


        } else {
            $(this).removeClass('opend');
            $('.drawers-wrapper .overlay').hide();
            $(this).next().show();
            $(".slide-nav").css("left", "-375px");
            var a = $(this).text().trim();
            $('.backslide').show().text(a);
            globalTab = $('#' + $(this).attr('id')).text().trim();
        }

        var activenavlink = 'GlobalNavigation:link' + navid;
        createCookie('activenavigationlink', activenavlink, 1);

        e.preventDefault();
    });

    $('.search-btn').on('click', function (e) {
        $('.expand-searchbar').animate({
            opacity: '1',
            width: '70%'
        }, 500).prev().fadeOut();
        e.preventDefault();
    });
	
    $('.expand-searchbar #d-close-searchbar').parent().on('click', function (e) {
        $('.expand-searchbar').animate({
            opacity: '0',
            width: '20%'
        }, 500).prev().fadeIn();
        e.preventDefault();
    });

    /*Footer Responsive script*/
    function footerResponse() {
        if ($(window).width() <= 992) {
            $('.list-header').each(function () {
                $(this).next().removeClass('in');
            });
        } else {
            $('.list-header').parent().find('ul').addClass('in').removeAttr('style');
        }
    }
    footerResponse();
	
    /*Sticky Navigation*/
    var scrollTop = ($('.sticky-nav').hasClass('sticky-nav')) ? parseInt($('.sticky-nav').offset().top) : 0;
    $('.sticky-nav .dropdown-menu li > a').click(function (e) {
        var hashtag = $(this.hash),
            $target = parseInt(hashtag.offset().top) - $('.sticky-navbar').outerHeight();
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });
	
    /*Home Header resize*/
    function headerResize() {
        var winheight = $(window).height(),
            headHeight = $('#page-header').outerHeight();
        $('.home-header').each(function () {
            ($(this).height() < winheight / 2 - headHeight && $(this).hasClass('small')) ? $(this).height(winheight / 2 - headHeight): false;
            ($(this).height() < winheight / 4 * 3 - headHeight && $(this).hasClass('medium')) ? $(this).height(winheight / 4 * 3 - headHeight): false;
            ($(this).height() < winheight - headHeight && $(this).hasClass('full')) ? $(this).height(winheight - headHeight): false;
        });


        if (!$('body').data('previewmode')) {
            $('.navigation-bar, .slideboxer, .backslide, .slide-nav, .drawers-wrapper, .tiles-list li > ul, body').removeAttr('style');
            $('.navigation-bar .nav > li a').removeClass('deactive');
            $(".overlay").stop().fadeOut();
        }

        /*Fixed Header - alignment*/
        $('#page-container').css('paddingTop', $('#page-header').outerHeight());
    }
    headerResize();
	
    /*Home Header - jump link*/
    $('.home-header .learn-link a').click(function (e) {
        var hashtag = $(this.hash),
            $target = (hashtag != '') ? parseInt(hashtag.offset().top) - $('.sticky-navbar').outerHeight() : 0;
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });
	
    /* Nav Content handling */
    /*Page Scroll*/
    $(window).scroll(function () {
        /*Navigtion Header offset*/
        if ($(this).scrollTop() >= 400) {
            $('header').css({
                'top': '-' + $('#page-header').outerHeight() + 'px'
            });
            $(".overlay").hide();
            $('.navigation-bar .nav > li a').removeClass('opend deactive').next().stop().fadeOut();
            $('.drawers-content').scrollTop(0);
        } else {
            $('header').css({
                'top': '0px'
            });
        }

        /*Sticky Nav scroll event*/
        ($(this).scrollTop() > scrollTop) ? $('.sticky-nav').addClass('navbar-fixed-top').show(): $('.sticky-nav').removeClass('navbar-fixed-top').hide();

        $('.sticky-nav .dropdown-menu li > a').each(function () {
            var scrolltag = $(this.hash),
                $target = (scrolltag != 'undefined') ? parseInt(scrolltag.offset().top - 1) + 45 : 0;
            if ($(window).scrollTop() >= $target) {
                $('.sticky-nav .dropdown-menu li > a').removeClass('current');
                $(this).addClass('current');
            }
        });
    });

    /*Responsive*/
    $(window).resize(function () {
        headerResize();
        footerResponse();
    });

    /*Navigation*/

    /* Creates and store the cookie */
    function createCookie(name, value, days) {
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            var expires = "; expires=" + date.toGMTString();
        } else {
            var expires = "";
        }
        document.cookie = name + "=" + value + expires + "; path=/";
    }

    // Navigation Close event /
    $('.drawer-close').on('click', function (e) {
        $("#page-container, #page-footer").stop().fadeIn();
        $('.drawers-wrapper, .overlay').stop().fadeOut();
        $('.drawers-content').scrollTop(0);
        $('.navigation-bar .nav > li:not(:last-child) > a').not($(this)).removeClass('deactive');
        e.preventDefault();
    });

    /*Mobile Navigation Events*/
    var globalTab = "";
    $(".menu").click(function (e) {
        $(".navigation-bar, #search-hide").stop().fadeIn();
        $(".slideboxer").css("left", "0");
        e.preventDefault();
    });
    $(".m-close-icon").click(function (e) {
        $('.slideboxer').css('left', '-375px');
        $(".navigation-bar").stop().fadeOut();
        $('.slide-nav').removeAttr('style');
        $('.drawers-wrapper, .backslide, .tiles-list li > ul').hide();
        $(".overlay").hide();
        e.preventDefault();
    });

    $(".tiles-list li > h3").click(function (e) {
        $(this).next().show();
        var a = $(this).text().trim();
        $('.backslide').text(a);
        $(".slide-nav").css("left", "-=375px");
        e.preventDefault();
    });
    $('.backslide').click(function (e) {
        $('.backslide').text(globalTab);
        $(".slide-nav").css("left", "+=375px");
        ($(".slide-nav").css("left") == '-375px') ? $(this).hide(): $(this).show();
        e.preventDefault();
    });
    $("#search-hide").click(function (e) {
        $(".advance-search, .search-overlay").show();
        $("#search-hide, .m-close-icon").hide();
        e.preventDefault();
    });
    $(".advance-search #m-close-searchbar").click(function (e) {
        $(".advance-search, .search-overlay").hide();
        $("#search-hide, .m-close-icon").show();
        e.preventDefault();
    });
    $(".advance-search").hide();
});
