/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:29-September-2016
    Version:2.1
*****************************************/
$j(function ($) {
    /**/
    /*Center Modal Dialog*/
    function reposition() {
        var modal = $(this),
            dialog = modal.find('.modal-dialog');
        modal.css('display', 'block');
        // Dividing by two centers the modal exactly, but dividing by three 
        // or four works better for larger screens.
        dialog.css("margin-top", Math.max(0, ($(window).height() - dialog.height()) / 2));
        $('.modal').on('shown', function () {
            //console.log('show');
            $('body').css({
                overflow: 'hidden'
            });
        }).on('hidden', function () {
            $('body').css({
                overflow: ''
            });
        });
    }
    // Reposition when a modal is shown
    $('.modal').on('show.bs.modal', reposition);
    // Reposition when the window is resized
    $(window).on('resize', function () {
        $('.modal:visible').each(reposition);
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
        ($(window).width() > 992) ? desktopNavigation(): mobileNavigation();
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

    /*Desktop Start*/
    function desktopNavigation() {
		
        $('.navigation-bar .nav > li.dropdown:not(:last-child) > a').click(function (e) {
            var navid = $(this).attr('id');            

            if ($(this).hasClass('opend')) {				
                $('.navigation-bar .nav > li:not(:last-child) > a').removeClass('opend deactive').next().stop().fadeOut();                
                $("#page-container, #page-footer").stop().fadeIn();
            } else {
				$(this).addClass('opend').removeClass('deactive').next().stop().fadeIn();	
				$('.navigation-bar .nav > li:not(:last-child) > a').not($(this)).removeClass('opend').addClass('deactive').next().stop().fadeOut();				
                $("#page-container, #page-footer").stop().fadeOut();
            }

            var activenavlink = 'GlobalNavigation:link' + navid;
            createCookie('activenavigationlink', activenavlink, 1);

            e.preventDefault();
        });
        // Navigation Close event /
        $('.drawer-close').on('click', function () {
            $("#page-container, #page-footer").stop().fadeIn();
            $('.drawers-wrapper').stop().fadeOut();
            $('.navigation-bar .nav > li:not(:last-child) > a').not($(this)).removeClass('deactive');
        });
    }
    /*End*/
    /*Mobile Start*/
    function mobileNavigation() {
		
        var globalTab = "";
        $(".menu").click(function (e) {
            $(".navigation-bar, #search-hide").stop().fadeIn();
            $(".slideboxer").css("left", "0");
            $("#page-container, #page-footer").stop().fadeOut();
            $(".roadside-block").removeClass("open_popup");
            e.preventDefault();
        });
        $(".m-close-icon").click(function (e) {
            $(".roadside-block").removeClass("open_popup");
            $("#page-container, #page-footer").stop().fadeIn();
            $('.slideboxer').css('left', '-375px');
            $(".navigation-bar").stop().fadeOut();
            $('.slide-nav').removeAttr('style');
            $('.drawers-wrapper, .backslide, .tiles-list li > ul').hide();
            $(".overlay").hide();
            e.preventDefault();
        });
        $(".navbar-nav > li > a").click(function (e) {
            $('.drawers-wrapper').hide();
            $(this).next().show();
            $('.backslide').show();
            $(".slide-nav").css("left", "-375px");
            var a = $(this).text().trim();
            $('.backslide').text(a);
            globalTab = $('#' + $(this).attr('id')).text().trim();
            e.preventDefault();
        });
        $(".tiles-list li > h3").click(function (e) {
            $(this).next().show();
            var a = $(this).text().trim();
            $('.backslide').text(a);
            $(".slide-nav").css("left", "-=375px");
            e.preventDefault();
        });
        $('.backslide').click(function () {
            $('.backslide').text(globalTab);
            $(".slide-nav").css("left", "+=375px");
            ($(".slide-nav").css("left") == '-375px') ? $(this).hide(): $(this).show();
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
        $(".overlay").hide();
        $('.roadside-text').click(function (e) {
            $(".roadside-block").toggleClass("open_popup");
            $(".slide-nav").removeClass("slidebox");
            $(".overlay").show();
            e.preventDefault();
        });
        $('.close_btn').click(function (e) {
            $(".roadside-block").toggleClass("open_popup");
            $(".slide-nav").removeClass("slidebox");
            $(".overlay").hide();
            e.preventDefault();
        });
    }
    /*End*/
});
