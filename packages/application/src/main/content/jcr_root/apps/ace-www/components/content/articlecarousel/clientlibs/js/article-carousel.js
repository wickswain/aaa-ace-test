$(function(){
	$('#article-carousel').find('.carousel-control.left')
						  .css({'cursor': 'wait',
								'pointer-events': 'none',
								'opacity': '0.1'});
													
	$('#article-carousel').bind('slid.bs.carousel', function (e) {
	
		var $this = $(this);

		$this.children('.carousel-control').show();
		$('#article-carousel').carousel('pause');
		
		$('.carousel-text .image-label').text($('div.active').data('imagelabel'));
		$('.carousel-text .author').text($('div.active').data('author'));
		
		$('#total-slides').text($('.carousel-inner .item').length);
		$('#current-slide').text($('div.active').index() + 1);

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