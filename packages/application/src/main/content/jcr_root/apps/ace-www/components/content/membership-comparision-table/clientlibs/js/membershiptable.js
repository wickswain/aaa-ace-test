$j(function($) {

    var showHideMembershipCols = function(){
		var numberOfRows = $(this).attr('data-rowcount');
        var fixedRows = $(this).attr('data-defaultrowcount');
        if (numberOfRows > fixedRows) {
            var showCount = parseInt(fixedRows) * 2;
            
            if($(window).width() < 992) {
                showCount = showCount - 1;
            }
            
            $(this).find('.membership-cols:gt(' + showCount + ')').hide();
            $(this).find(".seeAllLink").show();
        } else {
            $(this).find(".seeAllLink").remove();
        }
    };

	$(".membership-comparision").each(function() {
		showHideMembershipCols.call(this);
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
    
        $(".membership-comparision").each(function() {
            showHideMembershipCols.call(this);
        });

    });
});