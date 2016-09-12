
$(".drawers-content").hide();
$("#nav-2").click(function () {
    $('.navbar-nav li a').css("opacity", "0.3");
    $(this).css("opacity", "3");
    // $('.list-inline').css("display", "none");
    /*$(".drawers-content").slideDown("slow", function () {
        //alert("hi");
        $('.list-inline').css("display", "visible");
        $(".list-inline").slideDown(800).delay(200).fadeIn(100);
    });*/
    $(".drawers-content").slideDown(800).delay(200).fadeIn(300);
    // $("#testing").slideUp(100).delay(600).fadeIn(300);
    //$("div.second").slideUp(300).fadeIn(400);
});