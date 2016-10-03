$(function() {
	function showHideMembershipCols() {
		var numberOfRows = $(".membership-comparision").attr('data-rowcount');
        var fixedRows = $(".membership-comparision").attr('data-defaultrowcount');
        if (numberOfRows > fixedRows) {
            var showCount = parseInt(fixedRows) * 2;

            if($(window).width() < 992) {
				showCount = showCount - 1;
            }

            $(".membership-comparision").find('.membership-cols:gt(' + showCount + ')').hide();
            $(".membership-comparision").find(".seeAllLink").show();
        } else {
            $(".seeAllLink").remove();
        }
	}
	
	$(".membership-comparision").each(function() {
		showHideMembershipCols();
    });
	
    $(".btn-info").each(function() {
        $(this).click(function() {
            var header = $(this).data("header");
            var bodyText = $(this).data("body");
            $("#myModal").find(".modal-title").text(header);
            $("#myModal").find(".modal-body").html(bodyText);
        });
    });
    
    $(".seeAll").click(function() {
        $(this).parent().parent().parent().find('.membership-cols').css("display", "flex");
        $(this).parent().parent().remove();
    });
    
    /*Responsive*/
    $(window).resize(function () {
        showHideMembershipCols();
    });
});