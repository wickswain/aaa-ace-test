$(function() {
	// Page paths information can be updated here.
	var landingpagepath = "";
	var searchlandingpagepath = "/search-results.html";
	var joinpagepath = "";
    var logoImagePath = "http://www.aaaprod.axis41.net/content/dam/ace/logo/orbit-logo.png";
    var califLogoImagePath = "http://www.aaaprod.axis41.net/content/dam/ace/logo/acsc-logo.png";

	// Environment information can be updated here.
	var protocol = "http://";
	var environment = "www";
	var subdomain = ".aaaprod.axis41.net";

    var validRegions = ['texas', 'calif', 'hawaii','newmexico','northernnewengland','alabama', 'tidewater', 'autoclubmo', 'eastcentral']

	var defaultregionname = "calif";

	function getRegionName() {

		//Get region in cookie
        var regionInCookie = getCookie('aaa-region');

        //Get region from referrer if available
        //var host = window.location.hostname;
        var host = document.referrer;
        var regionInReferrer;


	    if (host) {
	        host = host.split('.');
	        if(host.length >= 3) {
	            regionInReferrer = host[1];            	
	        }            
	    }

		//if referrer is present and valid, it has higher priority as user might have changed the region
        if(regionInReferrer && ($.inArray(regionInReferrer, validRegions)>=0 ))
        {
			//set it in cookie and return it. 
			setCookie('aaa-region' , regionInReferrer, 1/24);
            return regionInReferrer;

        }else
        {
            //referrer is not present or invalid, use from cookie if present
            if (regionInCookie){
				return regionInCookie;
            }else
            {
				//set default in cookie untill we get from referrer 
				setCookie('aaa-region' , defaultregionname, 1/24);

            }
        }

	    return defaultregionname;
    }

	var regionname = getRegionName();

	function getHostName() {
        return protocol + environment + "." + regionname + subdomain;
    }
	var hostname = getHostName();

	var logoPath = "";
    if (regionname === 'calif')
    {
        logoPath = califLogoImagePath;
    }else
    {
        logoPath = logoImagePath;
    }

	
	$('#logo-link').find('img').attr('src', logoPath);
    $('#user-login').attr('href', "http://apps." + regionname + ".aaa.com/aceapps/account/my-account");
    $('#m-user-login').attr('href', "http://apps." + regionname + ".aaa.com/aceapps/account/my-account");
	$('#logo-link').attr('href', hostname + landingpagepath);
	$('#find-a-branch-link').attr('href', 'http://locator.aaa.com/');
	$('#m-find-a-branch-link').attr('href', 'http://locator.aaa.com/');
	$('#contact-us-link').attr('href', hostname + '/contact-us.html');
	$('#m-contact-us-link').attr('href', hostname + '/contact-us.html');
	$('#truck-link').attr('href', hostname + '/automotive/roadside-assistance.html');
    
    // Need to remove the port number if it is not required once this code moves to production environment
    $('#nav-0').attr('href', hostname + landingpagepath + "?navigationLink=nav-0");
    $('#nav-1').attr('href', hostname + landingpagepath + "?navigationLink=nav-1");
    $('#nav-2').attr('href', hostname + landingpagepath + "?navigationLink=nav-2");
    $('#nav-3').attr('href', hostname + landingpagepath + "?navigationLink=nav-3");
    $('#nav-4').attr('href', hostname + landingpagepath + "?navigationLink=nav-4");
    $('#nav-5').attr('href', hostname + landingpagepath + "?navigationLink=nav-5");
    $('#nav-6').attr('href', hostname + landingpagepath + "?navigationLink=nav-6");
    $('#nav-7').attr('href', joinpagepath);
    $('#m-join-btn').attr('href', joinpagepath);

    // Search functionality
    var searchkeyValue = "";
    $("#searchKey").keyup(function(event) {
        if (event.keyCode == 13) {
            searchkeyValue = $("#searchKey").val();
            if (searchkeyValue) {
                window.location.href = hostname + searchlandingpagepath + '?q=' + searchkeyValue;
            } else {
                alert("Please eneter search keyword");
            }
        }
    });
    
    $("#m-searchKey").keyup(function(event) {
        if (event.keyCode == 13) {
            searchkeyValue = $("#m-searchKey").val();
            if (searchkeyValue) {
                window.location.href = hostname + searchlandingpagepath + '?q=' + searchkeyValue;
            } else {
                alert("Please eneter search keyword");
            }
        }
    });
    
    $("#m-search-btn").click(function(event) {
    	searchkeyValue = $("#m-searchKey").val();
        if (searchkeyValue) {
            window.location.href = hostname + searchlandingpagepath + '?q=' + searchkeyValue;
        } else {
            alert("Please eneter search keyword");
        }
    });

    $(".menu").click(function(e) {
        $(".navigation-bar, #search-hide").stop().fadeIn();
        $(".slideboxer").css("left", "0");
        e.preventDefault();
    });
    
    $(".m-close-icon").click(function(e) {
        $('.slideboxer').css('left', '-375px');
        $(".navigation-bar").stop().fadeOut();
        $('.slide-nav').removeAttr('style');
        $('.drawers-wrapper, .backslide, .tiles-list li > ul').hide();
        $(".overlay").hide();
        e.preventDefault();
    });
    
    $('.search-btn').on('click', function(e) {
        $('.expand-searchbar').animate({
            opacity: '1',
            width: '70%'
        }, 500).prev().fadeOut();
        e.preventDefault();
    });
    
    $('.expand-searchbar #d-close-searchbar').parent().on('click', function(e) {
        $('.expand-searchbar').animate({
            opacity: '0',
            width: '20%'
        }, 500).prev().fadeIn();
        e.preventDefault();
    });
    
    $("#search-hide").click(function(e) {
        $(".advance-search, .search-overlay").show();
        $("#search-hide, .m-close-icon").hide();
        e.preventDefault();
    });
    
    $(".advance-search #m-close-searchbar").click(function(e) {
        $(".advance-search, .search-overlay").hide();
        $("#search-hide, .m-close-icon").show();
        e.preventDefault();
    });

    $(".advance-search").hide();

    function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
	}

    function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

});
