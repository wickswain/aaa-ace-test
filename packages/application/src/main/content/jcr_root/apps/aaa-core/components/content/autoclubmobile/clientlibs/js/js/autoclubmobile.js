$j(function($) {

    var OLD_APP_CLUBS = ["alabama", "texas", "autoclubmo", "eastcentral"];
    var BASE_APPLE_URL = "https://itunes.apple.com/app/apple-store/";

    $(document).ready(function(){
       var _location =  window.location.hostname;
       var appURL = (getAppURL(_location)) ? BASE_APPLE_URL + "id1094393125?pt=205244&ct=Autoclub&mt=8" : BASE_APPLE_URL + "id310730297?mt=8";

       $(".ios-link").attr("href", appURL);
    });

    function getAppURL(club){
        var isNewApp = true;
        for(var i = 0; i < OLD_APP_CLUBS.length; i++){
            if(OLD_APP_CLUBS[i].lastIndexOf(club) > -1){
                isNewApp = false;
            }
        }
        return isNewApp;
    }

});