var interval = setInterval(function() {
	var showHideMembershipCols = function(){
	    var numberOfRows = $(this).attr('data-rowcount');
	    var fixedRows = $(this).attr('data-defaultrowcount');

	    if (numberOfRows > fixedRows) {
	    	var showCount = parseInt(fixedRows) * 2;

	        $(this).find('.membership-cols:gt(' + showCount + ')').hide();
	        $(this).find(".seeAllLink").show();
	    } else {
	    	$(this).find(".seeAllLink").remove();
	    }
	};

    $(".membership-comparison").each(function() {
        showHideMembershipCols.call(this);
    });

    if(document.readyState === 'complete') {
        $(".seeAll").click(function() {
            $(this).parent().parent().parent().find('.membership-cols').css("display", "flex");
            $(this).parent().parent().remove();
        });

		clearInterval(interval);
    }

}, 100);

$(document).on("click", ".btn-info", function () {
	var header = $(this).data("header");
    var bodyText = $(this).data("body");

    if(header) {
        $(".modal-title").html(header);
    } else {
        $(".modal-title").html('');
    }
    
    if(bodyText) {
        $(".modal-body").html(bodyText);
    } else {
        $(".modal-body").html('');
    }
});
