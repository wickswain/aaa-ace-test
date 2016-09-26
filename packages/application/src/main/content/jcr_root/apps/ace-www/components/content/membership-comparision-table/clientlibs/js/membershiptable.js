$(function () {
            var numberOfRows = $('.membership-cols').length;
            var fixedRows = $('.membership-comparision').attr('data-index');
            if (numberOfRows > fixedRows) {
                var numberOfRows = $('.membership-cols').length;
                var showCount = parseInt(fixedRows - 1);
                $('.membership-cols:gt(' + showCount + ')').hide();
                $("#seeAll").show();
            }
            else {
                $("#seeAll").remove();
            }
            $(".seeAll").click(function () {
                $('.membership-cols').show();
                $('.membership-cols').css("display", "block");
                $("#seeAll").remove();
            });
        });