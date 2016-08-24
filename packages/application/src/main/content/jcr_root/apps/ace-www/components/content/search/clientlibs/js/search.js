(function(document, $) {
    "use strict";

    $(document).on("ready", function(e) {


	$( "#searchSubmit" ).click(function() {
		var searckData = $( "#searchKey").val();
        var region = getParameterByName('region');
		if(searckData){			
			window.location.href = $("#result-page").val() + '.html' + '?searchKeyValue='+searckData+"&region="+region;
		}else{
			alert('Please enter search key');
		}
	});

	$("#searchKey").keyup(function (event) {
			if (event.keyCode == 13) {
				$('#searchSubmit').click();
			}
		});});

})(document,Granite.$);
