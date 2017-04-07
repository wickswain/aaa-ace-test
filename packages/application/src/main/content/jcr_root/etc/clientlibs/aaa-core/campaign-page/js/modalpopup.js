var loadModalPopup = function () {
	var closePopup = $('.close');

	$(document).keyup(function (e) {		
		if (e.keyCode === 27) closePopup.trigger('click'); // esc key - modal close
	});
	$(document).click(function () {

		if ($('body').hasClass('modal-open')) {
			var modalBackDrop = $(this).find('.modal-backdrop');
			var len = modalBackDrop.length;
			if (len > 1) {				
				modalBackDrop.eq(0).remove();
			}
		}
	});

	$('.modal-body, .modal-header').click(function (e) {
		e.preventDefault();
	});

}

$(document).ready(function () {
	loadModalPopup();
});