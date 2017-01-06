/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:07-Nov-2016
    Version:5
*****************************************/
$j(function($) {
    /* ThirdParty Navigation url reading */
    var thirdPartyNav;

    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }
    /* Pagination Controls */
    thirdPartyNav = getParameterByName('navigationLink');
    if (thirdPartyNav) {
        $("#page-container").hide();
        $("footer").hide();
    } else {
        $("#page-container").show();
        $("footer").show();
    }
    $('.search-btn').on('click', function(e) {
        $('.expand-searchbar').animate({
            opacity: '1',
            width: '70%'
        }, 500).prev().fadeOut();
        e.preventDefault();
    });
    $('#d-close-searchbar').parent().on('click', function(e) {
        $('.expand-searchbar').animate({
            opacity: '0',
            width: '20%'
        }, 500).prev().fadeIn();
        e.preventDefault();
    });
    /*Footer Responsive script*/
    function footerResponse() {
        if ($(window).width() <= 992) {
            $('.list-header').each(function() {
                $(this).next().removeClass('in');
            });
        } else {
            $('.list-header').parent().find('ul').addClass('in').removeAttr('style');
        }
    }
    footerResponse();
    /*Sticky Navigation*/
    var scrollTop = ($('.sticky-nav').hasClass('sticky-nav')) ? parseInt($('.sticky-nav').offset().top) : 0;
    $('.sticky-nav .dropdown-menu li > a').click(function(e) {
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
        $('.home-header').each(function() {
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
    $('.home-header .learn-link a').click(function(e) {
        var hashtag = $(this.hash),
            $target = (hashtag != '') ? parseInt(hashtag.offset().top) - $('.sticky-navbar').outerHeight() : 0;
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });
    /* Nav Content handling */
    /*Page Scroll*/
    $(window).scroll(function() {
        /*Navigation Header offset*/
        if ($(this).scrollTop() >= 400 && $(window).width() > 768 && !thirdPartyNav) {
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
        $('.sticky-nav .dropdown-menu li > a').each(function() {
            var scrolltag = $(this.hash),
                $target = (scrolltag != 'undefined') ? parseInt(scrolltag.offset().top - 1) + 45 : 0;
            if ($(window).scrollTop() >= $target) {
                $('.sticky-nav .dropdown-menu li > a').removeClass('current');
                $(this).addClass('current');
            }
        });
    });
    /*Responsive*/
    $(window).resize(function() {
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
    /* Navigation Close event */
    $('.drawer-close').on('click', function(e) {
        if (!thirdPartyNav) {
            $("#page-container, #page-footer").stop().fadeIn();
            $('.drawers-wrapper, .overlay').stop().fadeOut();
            $('.drawers-content').scrollTop(0);
            $('.navigation-bar .nav > li:not(:last-child) > a').not($(this)).removeClass('deactive');
        } else {
            history.go(-1);
        }
        e.preventDefault();
    });
    /*Mobile Navigation Events*/
    var globalTab = "";
    $(".menu").click(function(e) {
        $(".navigation-bar, #search-hide").stop().fadeIn();
        $("body").addClass("modal-open");
        $(".slideboxer").css("left", "0");
        e.preventDefault();
        subnav();
    });
    $(".m-close-icon").click(function(e) {
        $("body").removeClass("modal-open");
        if (thirdPartyNav) {
            history.go(-1);
        } else {
            $('.slideboxer').css('left', '-375px');
            $(".navigation-bar").stop().fadeOut();
            $('.slide-nav').removeAttr('style');
            $('.drawers-wrapper, .backslide, .tiles-list li > ul').hide();
            $(".overlay").hide();
        }
        e.preventDefault();
    });

    function subnav() {
        $('.drawers-tiles').each(function() {
            $('.tiles-list > li > h3').click(function(e) {
                $(this).next().show();
                var a = $(this).text().trim();
                $('.backslide').text(a);
                $(".slide-nav").css("left", "-=375px");
                $(".slide-nav").scrollTop();
                e.preventDefault();
            });
        });
    }
    $('.backslide').click(function(e) {
        $('.backslide').text(globalTab);
        $(".slide-nav").css("left", "+=375px");
        ($(".slide-nav").css("left") == '-375px') ? $(this).hide(): $(this).show();
        $(".slide-nav").scrollTop();
        e.preventDefault();
    });
    $("#search-hide").click(function(e) {
        $(".advance-search, .search-overlay").show();
        $("#search-hide, .m-close-icon").hide();
        e.preventDefault();
    });
    $(".advance-search #m-close-searchbar").click(function(e) {
        $(".advance-search, .search-overlay").hide();
        $("#search-hide, .m-close-icon").show();
        e.preventDefault();
    });
    $(".advance-search").hide();

    function managaeNav(navid) {
        if ($(window).width() > 992) {
            if ($("#" + navid).hasClass('opend')) {
                if (!thirdPartyNav) {
                    $('.navigation-bar .nav > li:not(:last-child) > a').removeClass('opend deactive').next().hide();
                    $(".overlay").hide();
                } else {
                    history.go(-1);
                }
            } else {
                $('.navigation-bar .nav > li:not(:last-child) > a').not($("#" + navid)).removeClass('opend').addClass('deactive').next().stop().hide();
                $("#" + navid).addClass('opend').removeClass('deactive').next().stop().show();
                $(".overlay").stop().fadeIn();
            }
        } else {
            $("#" + navid).removeClass('opend');
            $('.drawers-wrapper .overlay').hide();
            $("#" + navid).next().show();
            $(".slide-nav").css("left", "-375px");
            $(".slide-nav").css("top", "0px");
            var a = $("#" + navid).text().trim();
            $('.backslide').show().text(a);
            globalTab = $('#' + $("#" + navid).attr('id')).text().trim();
        }
        var activenavlink = 'GlobalNavigation:link' + navid;
        createCookie('activenavigationlink', activenavlink, 1);
    }
    if (thirdPartyNav) {
        if ($(window).width() < 768 && thirdPartyNav == "nav-mobile") {
            $('.menu').click();
        } else if ($(window).width() > 768) {
            managaeNav(thirdPartyNav);
        } else {
            $('.menu').click();
            managaeNav(thirdPartyNav);
        }
    }
    $('.navigation-bar .nav > li.dropdown:not(:last-child) > a').click(function(e) {
        var navid = $(this).attr('id');
        managaeNav(navid);
    });
	$(".membership-cols .col-md-3").find("div").css("flex":"auto", "text-align": "inherit");
});