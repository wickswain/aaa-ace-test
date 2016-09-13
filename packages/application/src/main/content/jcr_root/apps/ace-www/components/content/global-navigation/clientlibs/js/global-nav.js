$(function () {
var i;
        for (i = 0; i < 3; ++i) {
            $(".nav-" + i).hide();
        }
        var checkCurrentnav = '';

        function manageNavContent(elem) {
            var getId = $(elem).attr("id");
            var getNav = getId.split("-");
            getNav = getNav[1];
            var i;
            for (i = 0; i < 3; ++i) {
                if (i != getNav) $(".nav-" + i).hide();
            }
            if (checkCurrentnav == getNav) {
                $(".nav-" + getNav).slideUp(500);
                $('.navbar-nav li a').css("opacity", "3");
                checkCurrentnav = '';
            }
            else {
                checkCurrentnav = getNav;
                $(".nav-" + getNav).slideDown(600).delay(200).fadeIn(300);
                //$(".nav-" + getNav).show();
                $('.navbar-nav li a').css("opacity", "0.3");
                $("#nav-" + getNav).css("opacity", "3");
            }
        }
        /* Close event */
        $('.close-drawer').on('click', function () {
            $(".nav-" + checkCurrentnav).slideUp(400);
            $('.navbar-nav li a').css("opacity", "3");
            checkCurrentnav = '';
        });
    });