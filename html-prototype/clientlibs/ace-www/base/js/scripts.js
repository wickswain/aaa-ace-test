/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:17-August-2016
    Version:0.1
*****************************************/
$(function () {
    $(window).resize(function () {
        footerResponse();
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
            }
            else {
                $('.list-header').parent().find('ul').addClass('in').removeAttr('style');
            }
        }
        /*$('.sticky-nav').attr({
            'data-offset-top': $('.sticky-nav').offset().top
        });*/
        // slide on click();
    $('.sticky-nav .tab-list li a').each(function () {
        var scrollto = $(this).attr('id')
            , $target = parseInt($(scrollto).offset().top);
        console.log(scrollto + ',' + $target);
        $(this).on('click', function () {
            $('html body').stop().animate({
                scrollTop: $target
            }, 1000, 'easeInOutExpo');
            return false;
        });
    });
    //Active state on scrolling upwards of select link
    $(window).scroll(function (e) {
        var windowpos = $(this);
        $('.sticky-nav .tab-list li a').each(function () {
            var scrollto = $(this).attr('id')
                , $target = parseInt($(scrollto).offset().top);
            if (windowpos.scrollTop() >= $target) {
                $('.sticky-nav .tab-list li a').removeClass('current');
                $(this).addClass('current');
            }
        });
        return false;
    });
});