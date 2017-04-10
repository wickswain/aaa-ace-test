/**********************************
    Template : AAA Scripts
    Created Date: 12-August-2016
    Modified Date: 03-Apr-2017
    Version: 5.1
****************************************/
$j(function($) {
    // ThirdParty Navigation url reading 
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

    // Pagination Controls 
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
    //Footer Responsive script
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
    //Sticky Navigation
    var scrollTop = 0;

    //Home Header resize
    function headerResize() {
        var winheight = $(window).height(),
            headHeight = $('#page-header').outerHeight();
        if (!$('body').data('previewmode')) {
            $('.navigation-bar, .slideboxer, .backslide, .slide-nav, .drawers-wrapper, .tiles-list li > ul, body').removeAttr('style');
            $('.navigation-bar .nav > li a').removeClass('deactive');
            $(".overlay").stop().fadeOut();
        }

        //Fixed Header - alignment
        $('#page-container').css('paddingTop', $('#page-header').outerHeight());
    }
    headerResize();

    //Home Header - jump link
    $('.home-header .learn-link a').click(function(e) {
        var hashtag = $(this.hash),
            $target = (hashtag != '') ? parseInt(hashtag.offset().top) - $('.sticky-navbar').outerHeight() : 0;
        $('html,body').stop().animate({
            scrollTop: $target
        }, 1000, 'swing');
        e.preventDefault();
    });

  /*$(document).on('click', '.drawers-container li a', function(){
     $("body").removeClass("modal-open");
  });*/

    // Nav Content handling 
    //Page Scroll

    $(window).scroll(function() {

        //Navigation Header offset

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

        //Sticky Nav scroll event

        /*$('.sticky-nav .dropdown-menu li > a').on('click', function(e) {
            var hashtag = $(this.hash),
                $target = parseInt(hashtag.offset().top) - $('.sticky-navbar').outerHeight();
            $('html,body').stop().animate({
                scrollTop: $target
            }, 1000, 'swing');
            e.preventDefault();
        });*/
        ($(this).scrollTop() > scrollTop) ? $('.sticky-nav').addClass('navbar-fixed-top').show(): $('.sticky-nav').removeClass('navbar-fixed-top').hide();
        /*$('.sticky-nav .dropdown-menu li > a').each(function() {

            var scrolltag = $(this.hash),
                $target = (scrolltag != 'undefined') ? parseInt(scrolltag.offset().top - 1) + 45 : 0;
            if ($(window).scrollTop() >= $target) {
                $('.sticky-nav .dropdown-menu li > a').removeClass('current');
                $(this).addClass('current');
            }
		});*/
    });

    //Responsive
    $(window).resize(function() {
        headerResize();
        footerResponse();
    });

    //Navigation
    // Creates and store the cookie 
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

    // Navigation Close event 
    $('.drawer-close .close-icon').on('click', function(e) {
        if (!thirdPartyNav) {
            $("#page-container, #page-footer").stop().fadeIn();
            $("body").removeClass("modal-open");
            $('.drawers-wrapper, .overlay').stop().fadeOut();
            $('.drawers-content').scrollTop(0);
            $('.navigation-bar .nav > li:not(:last-child) > a').not($(this)).removeClass('deactive');
        } else {
            history.go(-1);
        }
        e.preventDefault();
    });

    //Mobile Navigation Events
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
        e.preventDefault();
        $('.backslide').text(globalTab);
        $(".slide-nav").css("left", "+=375px");
        ($(".slide-nav").css("left") == '-375px') ? $(this).hide(): $(this).show();
        $(".slide-nav").scrollTop();
        if ($(".slide-nav").css("left") < "-375px") {
            $(this).hide();
            $(".slide-nav").css("left", "0px");
        }
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
                $("body").removeClass("modal-open");
            } else {
                $('.navigation-bar .nav > li:not(:last-child) > a').not($("#" + navid)).removeClass('opend').addClass('deactive').next().stop().hide();
                $("#" + navid).addClass('opend').removeClass('deactive').next().stop().show();
                $(".overlay").stop().fadeIn();
                $("body").addClass("modal-open");
            }
        } else {
            $("#" + navid).removeClass('opend');
            $('.drawers-wrapper .overlay, .drawers-wrapper').hide();
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

});

/**
 * Jump Link Target
 */
function jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime){
	var $target;
    if($(hashtag).length){
        if ($(hashtag).offset().top > navbarHeight) {
            $target = parseInt($(hashtag).offset().top) - stickyNavbarHeight;
        }
        else {
            $target = parseInt($(hashtag).offset().top) - navbarHeight;
        }
        $('html,body').stop().animate({
            scrollTop: $target
        }, swingTime, 'swing');
    }
    else {
		 $('html,body').stop().animate({
            scrollTop: 0
        }, 0, 'swing');
    }
}

$(document).on('click', '.sticky-nav .dropdown-menu li > a', function(e) {
var hashtag = $(this.hash), navbarHeight = $('.navbar-fixed-top').outerHeight(), stickyNavbarHeight = $('.sticky-nav').outerHeight(), swingTime = 1000;
jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
e.preventDefault();
});

$('.drawers-container li > a').on('click', function(e) {
var hashtag = $(this.hash), navbarHeight = $('.navbar-fixed-top').outerHeight(), stickyNavbarHeight = $('.sticky-nav').outerHeight(), swingTime = 0;
$("body").removeClass("modal-open");
jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
e.preventDefault();
});

$(document).ajaxComplete(function(e) {
var hashtag = window.location.hash, navbarHeight = $('.navbar-fixed-top').outerHeight(), stickyNavbarHeight = $('.sticky-nav').outerHeight(), swingTime = 0;
jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
e.preventDefault();
});

/**
 * Jump Link Target - End
 */

$('a').click(function() {
    var modalId;
    var isModalLink = $(this).has('span.modal-popup');

    if (isModalLink.length === 1) {
        modalId = $(this).attr('href');
    } else {
        isModalLink = $(this).parent('span.modal-popup');

        if (isModalLink.length === 1) {
            modalId = $(this).attr('href');
        }
    }

    if (modalId) {
        if (modalId.indexOf('#') === -1) {
            modalId = '#' + modalId;
        }
        $(modalId).modal('show');
    }
});

var currentPageUrl = window.location.href;
var signOutUrl = $("#signOutUrl").val();
var signInUrl = $("#signInUrl").val();

if (signInUrl != null && signOutUrl != null) {
    signOutUrl = signOutUrl + "?ReturnURL=" + currentPageUrl;
    signInUrl = signInUrl + "?ReturnURL=" + currentPageUrl;

    $.ajax({
        type: "POST",
        url: "/bin/aaa/userlogin",
        dataType: 'json',
        success: function(result) {
            if (result.isLoggedIn) {
                $("#user-logout").attr("href", signOutUrl);
                $("#firstName").text(result.firstName);
                $("#firstName-profile").text(result.firstName);
                //Mobile Login
                $(".m-user-details .avatar-details .signout #user-logout").attr("href", signOutUrl);
                $(".m-user-details .avatar-details .restrict-characters").text(result.firstName);
            } else {
                $("#user-login").attr("href", signInUrl);
                $(".m-user-details .sign-join-in .user-login").attr("href", signInUrl);
            }
        }
    });
}