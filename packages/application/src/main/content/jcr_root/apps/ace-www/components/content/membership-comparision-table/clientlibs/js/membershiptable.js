$(function ()  {
            $(".membership-comparision").each(function () {
                var numberOfRows = $(this).find('.membership-cols').length;
                var fixedRows = $(this).attr('data-index');
                
                if (numberOfRows > fixedRows) {
                    var numberOfRows = $('.membership-cols').length;
                    var showCount = parseInt(fixedRows - 1);
                    $(this).find('.membership-cols:gt(' + showCount + ')').hide();
                    $(this).find(".seeAllLink").show();
                } else {
                    $(".seeAllLink").remove();
                }
            });
			
			$(".btn-info").each(function() {
				$(this).click(function() {
					var header = $(this).data("header");
					var bodyText = $(this).data("body");
					
					$("#myModal").find(".modal-title").text(header);
					$("#myModal").find(".modal-body").html(bodyText);
				});
			});
			
            $(".seeAll").click(function () {
                $(this).parent().parent().parent().find('.membership-cols').show();
				$(this).parent().parent().remove();
            });
        });