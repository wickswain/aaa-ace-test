/*****************************************
    Template : AAA Scripts
    Created Date:12-August-2016
    Modified Date:31-August-2016
    Version:0.2
*****************************************/
$(function () {
    
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
	var scrollTop = parseInt($('.sticky-nav').offset().top);
	
	var stickynav = function(){		
		$('.sticky-nav').width($('.container-custom').width());
	};
	
	$('.sticky-nav .dropdown-menu a').click(function (e) {
        var target = $(this.hash);      
		$('html,body').stop().animate({
			scrollTop: target.offset().top - $('.sticky-nav').height()
		}, 1000,'swing');	         
		$('.sticky-nav .dropdown-menu a').removeClass('current');
		$(this).addClass('current');
        e.preventDefault();
    });
	
	/*Page Scroll*/
	$(window).scroll(function(){		
		($(this).scrollTop() > scrollTop)? $('.sticky-nav').addClass('navbar-fixed-top'):	$('.sticky-nav').removeClass('navbar-fixed-top');
		
	});
	
	/*Responsive*/
    $(window).resize(function () {
        footerResponse();
        stickynav();
    });
	
	
});
