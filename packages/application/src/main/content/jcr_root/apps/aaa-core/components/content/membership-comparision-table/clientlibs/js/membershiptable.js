$(".membership-row-text, .membership-row-img, .icon-info").find("div").css("padding", "0px").css("border", "none").css("display", "inherit");
$(".membership-row-text, .membership-row-img, .icon-info").find("span").css("padding", "0px").css("border", "none");

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
