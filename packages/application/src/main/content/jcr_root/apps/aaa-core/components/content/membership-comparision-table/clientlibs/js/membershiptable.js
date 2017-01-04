$j(function($) {
	var showHideMembershipCols = function(){
	    var numberOfRows = $(this).attr('data-rowcount');
	    var fixedRows = $(this).attr('data-defaultrowcount');
	    	
	    if (numberOfRows > fixedRows) {
	    	var showCount = parseInt(fixedRows - 1);
	
	        $(this).find('.membership-cols:gt(' + showCount + ')').hide();
	        $(this).find(".seeAllLink").show();
	    } else {
	    	$(this).find(".seeAllLink").remove();
	    }
	};

    $(".membership-comparision").each(function() {
    	showHideMembershipCols.call(this);
    });
	
    $(".seeAll").click(function() {
        $(this).parent().parent().parent().find('.membership-cols').css("display", "flex");
        $(this).parent().parent().remove();
    });
});

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
