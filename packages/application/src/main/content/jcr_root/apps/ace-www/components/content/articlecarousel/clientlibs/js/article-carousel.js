$j(function($){

	$('#article-carousel').find('.carousel-control.left')
						  .css({'cursor': 'wait',
						        'pointer-events': 'none',
								'opacity': '0.1'});
	
	$('.carousel-text .image-label').text($('div.active').data('imagelabel') + ' | ' + $('div.active').data('author'));
	$('.right-align').text($('div.active').index() + 1 + ' of ' + $('.carousel-inner .item').length);
								
	$('#article-carousel').bind('slid.bs.carousel', function (e) {
    
        var $this = $(this);

        $this.children('.carousel-control').show();
        $('#article-carousel').carousel('pause');
		
		$('.carousel-text .image-label').text($('div.active').data('imagelabel') + ' | ' + $('div.active').data('author'));
		$('.right-align').text($('div.active').index() + 1 + ' of ' + $('.carousel-inner .item').length);
	
		
        if ($('.carousel-inner .item:first').hasClass('active')) {
            $this.children('.carousel-control.left')
				 .css({'cursor': 'wait',
				       'pointer-events': 'none',
					   'opacity': '0.1'});
        } else {
            $this.children('.carousel-control.left').removeAttr('style');
        }
        
        if ($('.carousel-inner .item:last').hasClass('active')) {
			$this.children('.carousel-control.right')
				 .css({ 'cursor': 'wait',
						'pointer-events': 'none',
						'opacity': '0.1'});
        } else {
            $this.children('.carousel-control.right').removeAttr('style');
        }
		
    });

});