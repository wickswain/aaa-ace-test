/*****************************************
    Template : AAA Scripts
    Created Date: 12-August-2016
    Modified Date: 20-APR-2017
    Version: 5.0
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
    var scrollTop = 0;

    /*Home Header resize*/
    function headerResize() {
        var winheight = $(window).height(),
            headHeight = $('#page-header').outerHeight();
        if (!$('body').data('previewmode')) {
            $('.navigation-bar, .slideboxer, .backslide, .slide-nav, .drawers-wrapper, .tiles-list li > ul, body').removeAttr('style');
            $('.navigation-bar .nav > li a').removeClass('deactive');
            $(".overlay").stop().fadeOut();
        }
        /*Fixed Header - alignment*/
        $('#page-container').css('paddingTop', $('#page-header').outerHeight());
    }
    headerResize();
    $(document).on('click', '.drawers-container li > a', function() {
        $("body").removeClass("modal-open");
    });
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
        /*Hide logo in tab and mobile device*/
        if ($(window).width() < 992) {
            ($(this).scrollTop() > scrollTop) ? $('.brand').hide(): $('.brand').show();
        }
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
    $('.drawer-close .close-icon').on('click', function(e) {
        if (!thirdPartyNav) {
            $("#page-container, #page-footer").stop().fadeIn();
            $("body").removeClass("modal-open");
            $('.drawers-wrapper, .overlay').stop().fadeOut();
            $('.drawers-content').scrollTop(0);
            /*Update naviagtion menu on click of naviagtion drawer*/
            $('.navigation-bar .nav > li:not(:last-child) > a').not($(this)).removeClass('opend deactive');
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
        e.preventDefault();
        $('.sub-title').next().hide();
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

function jumpLinkTarget(hashtagKey, navbarHeight, stickyNavbarHeight, swingTime) {
    var $target;
    if (hashtagKey && $(hashtagKey).length > 0) {
        if ($(hashtagKey).offset().top > navbarHeight) {
            $target = parseInt($(hashtagKey).offset().top) - stickyNavbarHeight;
        } else {
            /*Open first component when auther clicks on jumplink*/
            $('.navigation-bar .nav > li:not(:last-child) > a').removeClass('opend deactive').next().hide();
            $target = parseInt($(hashtagKey).offset().top) - navbarHeight;
        }
        $('html,body').stop().animate({
            scrollTop: $target
        }, swingTime, 'swing');
    } else {
        $('.drawers-wrapper, .overlay').stop().fadeOut();
        $('.drawers-content').scrollTop(0);
        $('.navigation-bar .nav > li:not(:last-child) > a').removeClass('opend deactive').next().hide();
        $('html,body').stop().animate({
            scrollTop: 0
        }, 0, 'swing');
    }
}

$(document).on('click', '.sticky-nav .dropdown-menu li > a, .learn-btn', function(e) {
    var hashtag = $(this.hash),
        navbarHeight = $('.navbar-fixed-top').height(),
        stickyNavbarHeight = $('.sticky-nav').height(),
        swingTime = 1000;
    if ($(this).attr('target') == '_self') {
        jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
    } else {
        jumpLinkTarget(hashtag, navbarHeight, stickyNavbarHeight, swingTime);
        e.preventDefault();
    }
});

function fetchURLWithQueryParams(url, queryStringParams, selectedQueryParams, customQueryParams, isHashTag) {
    var hashtagValue = '';

    if (isHashTag) {
		hashtagValue = "#" + url.split('#')[1];
    }

	if (url !== '') {
		// Append the custom query parameters if any
        $.each(customQueryParams, function( index, value ) {
            var customQueryParamValue = value;
            
            $.each(queryStringParams, function( queryStringIndex, queryStringValue ) {
                if (queryStringValue.match(customQueryParamValue)) {
                	url = getQueryStringValueConcatenatedURL(queryStringValue, url, isHashTag);
                }
            });
		});

		// Append the selected query parameters if any
		$.each(selectedQueryParams, function( index, value ) {
            var selectedQueryParamValue = value;
            
            $.each(queryStringParams, function( queryStringIndex, queryStringValue ) {
                if (queryStringValue.match(selectedQueryParamValue)) {
                    url = getQueryStringValueConcatenatedURL(queryStringValue, url, isHashTag);
                }
            });
		});
	}

    if (isHashTag) {
		url = url + hashtagValue;
    }

	return url;
}

function getQueryStringValueConcatenatedURL(queryStringValue, url, isHashTag) {
	if (url.match("/?")) {
        if (isHashTag) {
			url = url.split('#', 1)[0] + "&" + queryStringValue;
        } else {
			url = url + "&" + queryStringValue;
        }
    } else {
        if (isHashTag) {
			url = [url.slice(0, url.indexOf('#')), "?" + queryStringValue].join('');
        } else {
			url = url + "?" + queryStringValue;
        }
    }

	return url;
}

$(document).on('click', '.drawers-container li > a, .tile-card, .btn-style, .link-btn', function(e) {
    var selfAccessBtn = 0,
        hashtag = this.hash.substr(1),
        hrefURL = $(this).attr('href'),
        newHreftag = hrefURL.split('#', 1)[0],
        pathName = window.location.pathname,
        navbarHeight = $('.navbar-fixed-top').height(),
        stickyNavbarHeight = $('.sticky-nav').height(),
        swingTime = 0,
        queryString = window.location.search;

    if (queryString !== '') {
        queryString = queryString.split("?")[1];
    	var selectedQueryParamKeys = [],
    		customQueryParamKeys = [],
            URLchanged = 0;
 
    	if ($(this).attr('data-selectedparams')) {
    		selectedQueryParamKeys = $(this).attr("data-selectedparams").split(",");
    	}

    	if ($(this).attr('data-customparams')) {
    		customQueryParamKeys = $(this).attr("data-customparams").split(",");
    	}

        // To prevent appending the request parameters from second time onwards set value to 1.
        $.each(selectedQueryParamKeys, function( selectedQueryStringIndex, selectedQueryStringValue ) {
            if (hrefURL.match(selectedQueryStringValue)) {
                URLchanged = 1;
            }
        });

        // To prevent appending the request parameters from second time onwards set value to 1.
        $.each(customQueryParamKeys, function( customQueryStringIndex, customQueryStringValue ) {
            if (hrefURL.match(customQueryStringValue)) {
                URLchanged = 1;
            }
        });

        if (URLchanged == 0) {
    		var queryStringParams = queryString.split("&");
    		var finalURLWithQueryParams = fetchURLWithQueryParams(hrefURL, queryStringParams, selectedQueryParamKeys, customQueryParamKeys, hashtag.length > 0);

            $(this).attr('href', finalURLWithQueryParams);
        }

    }

    if (newHreftag == '') {
        selfAccessBtn = 1;
    } else if (newHreftag != '' && (newHreftag==pathName)) {
        selfAccessBtn = 1;
    } else {
        selfAccessBtn = 0;
    }

    if ($(this).attr('target') == '_blank') {
        $("#page-container, #page-footer").stop().fadeIn();
        $("body").removeClass("modal-open");
        $('.drawers-wrapper, .overlay').stop().fadeOut();
        $('.drawers-content').scrollTop(0);
        $('.navigation-bar .nav > li:not(:last-child) > a').removeClass('opend deactive').next().hide();
        $('.slideboxer').css('left', '-375px');
        $(".navigation-bar").stop().fadeOut();
        $('.slide-nav').removeAttr('style');
        $(".overlay").hide();
    } else if ($(this).attr('target') == '_self' && hashtag.length > 0 && selfAccessBtn == 1) {
        var target = $(this).attr('href');
        if (target.length != 0) {
            var hashtagKey = this.hash;
            jumpLinkTarget(hashtagKey, navbarHeight, stickyNavbarHeight, swingTime);
        }
        $('.slideboxer').css('left', '-375px');
        $(".navigation-bar").hide();
        $('.slide-nav').removeAttr('style');
        $(".overlay").hide();
        e.preventDefault();
    } else {
        $('.slideboxer').css('left', '-375px');
        $(".navigation-bar").hide();
        $('.slide-nav').removeAttr('style');
        $(".overlay").hide();
    }
});

$(document).on("click", "a", function() {
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
                /*Mobile Login*/
                $(".m-user-details .avatar-details .signout #user-logout").attr("href", signOutUrl);
                $(".m-user-details .avatar-details .restrict-characters").text(result.firstName);
            } else {
                $("#user-login").attr("href", signInUrl);
                $(".m-user-details .sign-join-in .user-login").attr("href", signInUrl);
            }
        }
    });
}