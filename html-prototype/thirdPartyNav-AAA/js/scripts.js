$(function() {
	var navlandingpagepath = "/content/ace-www/en.html";
	
	function getRegionName() {
    	var regionName = "calif";
        var host = window.location.hostname;
	    
	    if (host) {
	        host = host.split('.');
	        if(host.length >= 2) {
	            regionName = host[1];            	
	        }            
	    }
	    
	    return regionName;
    }
	var regionname = getRegionName();
	
	function getHostName() {
		var protocol = "http://";
		var environment = "ace-www-dev-publish";
		var subdomain = ".aaa.com";
        
        return protocol + environment + "." + regionname + subdomain;
    }
	var hostname = getHostName();
	
    $('#user-login').attr('href', "http://apps." + regionname + ".aaa.com/aceapps/authenticate/login?ReturnURL=" + window.location.href);
    $('#my-account').attr('href', "http://apps." + regionname + ".aaa.com/aceapps/account/my-account");
    $('#sign-out').attr('href', "http://apps." + regionname + ".aaa.com/aceapps/authenticate/logout?ReturnURL=" + window.location.href);

    // Need to remove the port number if it is not required once this code moves to production environment
    $('#nav-0').attr('href', hostname + ":4503" + navlandingpagepath + "?navigationLink=nav-0");
    $('#nav-1').attr('href', hostname + ":4503" + navlandingpagepath + "?navigationLink=nav-1");
    $('#nav-2').attr('href', hostname + ":4503" + navlandingpagepath + "?navigationLink=nav-2");
    $('#nav-3').attr('href', hostname + ":4503" + navlandingpagepath + "?navigationLink=nav-3");
    $('#nav-4').attr('href', hostname + ":4503" + navlandingpagepath + "?navigationLink=nav-4");
    $('#nav-5').attr('href', hostname + ":4503" + navlandingpagepath + "?navigationLink=nav-5");
    $('#nav-6').attr('href', hostname + ":4503" + navlandingpagepath + "?navigationLink=nav-6");
	
    // Login functionality
    var isLoggedIn = false;
    var url = hostname + ":4503/user.loginstatus.json";
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json',
        success: function(response) { 
			isLoggedIn = response.isLoggedIn;
        },
        async: false
    });
    
    if(isLoggedIn){
    	var username = getUserName();
    	
    	$('#user-login').css('display', 'none');
    	$('#user-info').css('display', 'block');
    	$('#username').html(username);
    } else {
    	$('#user-login').css('display', 'block');
    	$('#user-info').css('display', 'none');
    	$('#username').html('');
    }
    
    function getUserName() {
    	var userCookie = getCookie('aceuser');
    	
    	return fetchCookieValue(userCookie, 'FirstName');
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
    
    function fetchCookieValue(cookieValue, key) {
    	var cookieItems = cookieValue.split('&');
    	
    	for(var i = 0; i < cookieItems.length; i++) {
            var cookieItem = cookieItems[i];
            
            if (cookieItem.contains(key)) {
                return cookieItem.substring(cookieItem.indexOf('=') + 1);
            }
        }
    	
    	return '';
    }

    // Search functionality
    var searchkeyValue = "";
    $("#searchKey").keyup(function(event) {
        if (event.keyCode == 13) {
            searchkeyValue = $("#searchKey").val();
            if (searchkeyValue) {
                window.location.href = hostname + ':4503/content/ace-www/en/google-search-results.html?searchKeyValue=' + searchkeyValue;
            } else {
                alert("Please eneter search keyword");
            }
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
});
