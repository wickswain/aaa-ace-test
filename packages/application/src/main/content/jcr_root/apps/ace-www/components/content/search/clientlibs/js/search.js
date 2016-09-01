(function(document, $) {
	"use strict";

	$(document).on("ready", function(e) {

				/* Google Search API functionality */
				//onClick search event
				var searchkeyValue = "";
				$(".pagination-grid").hide();
				$("#searchSubmit").click(
						function() {
							searchkeyValue = $("#searchKey").val();
					        
							localStorage.setItem("metaName", '');
					        localStorage.setItem("metaTag", '');
							
					        if (searchkeyValue) {
								window.location.href = $("#result-page").val() + '.html' + '?q=' + searchkeyValue;
							} else {
								alert('Please enter search key');
							}
						});
				
				//onEnter search event
				$("#searchKey").keyup(function(event) {
					if (event.keyCode == 13) {
						$('#searchSubmit').click();
					}
				});//end of key up

			}); // end of document.on ready

})(document, Granite.$);
