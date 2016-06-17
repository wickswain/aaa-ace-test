/**
*
*
* ${projectName} - Javascript Functions
*
**/

/** Message Boxes **/ 
function test(){alert('test function');}

(function($) {

/*------ Header ------*/

	// Set position of dropdown menus
	$('.dropdown').each(function() {
		var parentWidth = $(this).parent().outerWidth(true);
		if ($(this).parent().is(':last-child')) {
      $(this).css('left', (parentWidth / 2) - 110);
    } else {
      $(this).css('left', (parentWidth / 2) - 100);
    }
	});

	// Dropdown Animation
	$('#main-nav div').on('mouseenter', function() {
		$(this).find('.dropdown').stop(true,false).fadeIn(200);
	});
	$('#main-nav div').on('mouseleave', function() {
		$('.dropdown').fadeOut(200);
	});

})(this.jQuery);