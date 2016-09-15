/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:13-September-2016
    Version:0.6
*****************************************/
$(function () {

    /*User Login dialog*/
    $('#user-login').on('click', function () {
        $('.user-info').show();
        $(this).hide();
    });
    $('.signout a').on('click', function (e) {
        $('.user-info').toggle();
        $('#nav-links').removeClass('open');
        $('#user-login').show();
        e.preventDefault();
    });
    $('.search-btn').on('click', function () {
        $('.expend-searchbar').animate({
            opacity: '1',
            width: '80%'
        }, 500).prev().fadeOut();
    });
    $('.expend-searchbar .close-icon').parent().on('click', function () {
        $('.expend-searchbar').animate({
            opacity: '0',
            width: '20%'
        }, 500).prev().fadeIn();
    });
    /*Fixed Header - alignment*/
    $('#page-container').css('paddingTop', $('#page-header').outerHeight());
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
    }
    headerResize();
    /*Home Header - jump link*/
    ($('.home-header').next().attr('id') != 'undefined') ? $('.learn-link a').attr('href', '#' + $('.home-header').parent().next().children().attr('id')): $('.learn-link a').attr('href', '#');
    $('.home-header .learn-link a').click(function (e) {
        var hashtag = $(this.hash),
            $target = (hashtag != '') ? parseInt(hashtag.offset().top) : 0;
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });
    /* Nav Content handling */
    ($('.drawers-content').height() < $(window).height() - $('header').outerHeight()) ? $('.drawers-wrapper').height($(window).height() - $('header').outerHeight()): false;
    /*Page Scroll*/
    $(window).scroll(function () {
        //console.log($(this).scrollTop());
        /*Sticky Header offset*/
        ($(this).scrollTop() >= 400) ? $('header').css({
            'top': '-' + $('#page-header').outerHeight() + 'px'
        }): $('header').css({
            'top': '0px'
        });

        /*Sticky Nav scroll event*/
        ($(this).scrollTop() > scrollTop) ? $('.sticky-nav').addClass('navbar-fixed-top').show(): $('.sticky-nav').removeClass('navbar-fixed-top').hide();
        $('.sticky-nav .dropdown-menu li > a').each(function () {
            var scrolltag = $(this.hash),
                $target = parseInt(scrolltag.offset().top - 1) + 45;
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
        stickynav();
    });
    /*Navigation*/
    $('.navigation-bar .nav li a').removeAttr('onclick');
    $('.navigation-bar .nav li a').click(function (e) {
        var navid = $(this).attr('id');
        $('.drawers-wrapper:not(".' + navid + '")').hide();
        $('.' + navid).stop().slideToggle(500);
        $('.navigation-bar .nav li a').css("opacity", "0.3");
        $(this).css("opacity", "1");

        var activenavlink = 'GlobalNavigation:link' + navid;
        createCookie('activenavigationlink', activenavlink, 1);

        e.preventDefault();
    });
    /* Navigation Close event */
    $('.drawer-close').on('click', function () {
        $(this).parent().parent().stop().slideUp();
        $('.navigation-bar .nav li a').css("opacity", "1");
    });
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
});
