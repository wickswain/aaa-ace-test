$(function () {
    // Page paths information can be updated here.
    var landingpagepath = "";
    var aaapagepath = "";
    var fbpagepath = "";
    var twpagepath = "";
    var logoimagepath = "//www.nothernnewengland.aaa.com/etc/clientlibs/aaa-thirdparty/full-footer/images/aaa-white-logo-xs.png";
    // Environment information can be updated here.
    var protocol = "//";
    var environment = "www";
    var subdomain = ".aaa.com";
    var defaultregionname = "calif";

    function getRegionName() {
        var regionName = defaultregionname;
        var host = window.location.hostname;
        if (host) {
            host = host.split('.');
            if (host.length >= 2) {
                regionName = host[1];
            }
        }
        return regionName;
    }
    //var regionname = getRegionName();
    var regionname = '';
    
    function getHostName() {
        //return protocol + environment + "." + regionname + subdomain;
    	return protocol + environment + subdomain;
    }
    var hostname = getHostName();
    $('#footer-logo').find('img').attr('src', logoimagepath);
    $('#footer-logo-link').attr('href', hostname + landingpagepath);
    $('#aaa-link').attr('href', hostname + aaapagepath);
    $('#fb-link').attr('href', fbpagepath);
    $('#tw-link').attr('href', twpagepath);
    $('#contact-us-link').attr('href', hostname + '/contact-us.html');
});