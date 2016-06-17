(function($) {

    $('.short-stack').on('click', function() {
        $('.mobile-nav-container').fadeIn(200);
        $('body').css('overflow', 'hidden');
    });
    $('#mobile-nav .sprite.close').on('click', function() {
        $('.mobile-nav-container').fadeOut(100);
        $('body').css('overflow', 'auto');
    });
    $('.mobile-dropdown').on('click', function(e) {
        var $this = $(this);
        if ( $this.hasClass('expanded') ) {
            $this.siblings('.dropdown').stop(true,false).animate({
                height : 0
            }, 200, function() {
                $this.removeClass('expanded');
            });
        } else {
            var navHeight = $this.siblings('.dropdown').find('.dropdown-item').length * 40;
            $this.addClass('expanded');
            $this.siblings('.dropdown').animate({
                height : navHeight
            }, 300);
        }
    });
    
    // Get the height of the header and add equal padding to .wrapper
    $(window).on('load resize', function() {
        var headerHeight = $('#header').height();
        $('.wrapper').css('padding-top', headerHeight);
    });

})(this.jQuery);