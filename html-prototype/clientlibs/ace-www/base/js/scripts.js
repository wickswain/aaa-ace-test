/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:22-September-2016
    Version:1.0
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

        $('.modal')
       .on('shown', function () {
           //console.log('show');
           $('body').css({ overflow: 'hidden' });
       })
       .on('hidden', function () {
           $('body').css({ overflow: '' });
       });
    }
    // Reposition when a modal is shown
    $('.modal').on('show.bs.modal', reposition);
    // Reposition when the window is resized
    $(window).on('resize', function () {
        $('.modal:visible').each(reposition);
    });

    /*User Login dialog*/
    $('#user-login').on('click', function (e) {
        $('.user-info').show();
        $(this).hide();
        e.preventDefault();
    });
    $('.signout a').on('click', function (e) {
        $('.user-info').toggle();
        $('#nav-links').removeClass('open');
        $('#user-login').show();
        e.preventDefault();
    });
    $('.search-btn').on('click', function (e) {
        $('.expand-searchbar').animate({
            opacity: '1',
            width: '70%'
        }, 500).prev().fadeOut();
        e.preventDefault();
    });
    $('.expand-searchbar .close-icon').parent().on('click', function (e) {
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
            ($(this).height() < winheight / 2 - headHeight && $(this).hasClass('small')) ? $(this).height(winheight / 2 - headHeight) : false;
            ($(this).height() < winheight / 4 * 3 - headHeight && $(this).hasClass('medium')) ? $(this).height(winheight / 4 * 3 - headHeight) : false;
            ($(this).height() < winheight - headHeight && $(this).hasClass('full')) ? $(this).height(winheight - headHeight) : false;
        });
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
        }) : $('header').css({
            'top': '0px'
        });

        /*Sticky Nav scroll event*/
        ($(this).scrollTop() > scrollTop) ? $('.sticky-nav').addClass('navbar-fixed-top').show() : $('.sticky-nav').removeClass('navbar-fixed-top').hide();
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
    $('.navigation-bar .nav li:not(:last-child) a').click(function (e) {
        var navid = $(this).attr('id');
        console.log(navid);
        $('.drawers-wrapper:not(".' + navid + '")').fadeOut();
        $('.' + navid).fadeToggle();
        //$('.navigation-bar').show(500);
        $('.navigation-bar .nav li:not(:last-child) a').removeClass('active');
        $('.navigation-bar .nav li:not(:last-child) a').not($(this)).addClass('active');

        var activenavlink = 'GlobalNavigation:link' + navid;
        createCookie('activenavigationlink', activenavlink, 1);

        e.preventDefault();
    });
    // Navigation Close event /
    $('.drawer-close').on('click', function () {
        $('.drawers-wrapper').fadeOut();
        $('.navigation-bar .nav li a').removeClass('active');
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
