$(".pagination-grid").hide();

var searchkeyValue = getParameterByName('q');	

	//Default Pagination Config details
        var resultsPerPage = 10;
        var pageNumber = 1;
        $('#prevPage').click(function () {
            getResultData(searchkeyValue, -1);
        });
        $('#nextPage').click(function () {
            getResultData(searchkeyValue, 1);
        });
        $(".pagination-input").change(function () {
            var pageNum = $("#pageNumber").val();
            if (Math.floor(pageNum) == pageNum && $.isNumeric(pageNum)) {
                //alert('yes its an int!');
                getResultData(searchkeyValue, pageNum);
            }
            else {
                alert('Please enter numeric value');
            }
        });

        if(searchkeyValue){
            getResultData(searchkeyValue,0);
        }	

        // Api Call based on page number
        function getResultData(searchkeyValue, paginationCount) {
            if (paginationCount === -1) {
                pageNumber--;
            }
            if (paginationCount === 1) {
                pageNumber++;
            }
            if (paginationCount === 0) {
                pageNumber = 1;
            }
            if (paginationCount > 1) {
                pageNumber = paginationCount;
            }

            var apikey = 'AIzaSyBC-G6Eu9j36HTIkkCcw92_CVg_ONtK3Ec' ;//'AIzaSyA5Q7Z1wbPOUa59IhoZYO52ulQjWAndn3I';
            var engineid = '002841571323135263873:_kstmmls-cu'; //'004650686028114475301:tlpfyltlij4';

            var hostname = window.location.hostname;

            var domain = hostname.substr(hostname.indexOf(".")+1,hostname.length)
            
            var url = "https://www.googleapis.com/customsearch/v1?key="+apikey+"&cx="+ engineid 
            			+ "&q=" + searchkeyValue + "&start=" + pageNumber 
            			+ "&siteSearch="+domain + "&callback=?";
            //+ "&siteSearch="+domain
            $.getJSON(url, '', SearchCompleted);
        }
        // Final result appending to our html
        function SearchCompleted(response) {
            if (response.error) {
                var errorMessage = response.error.errors[0].message;
                alert(errorMessage);
            }
            else {
                var html = "";
                if (response.items == null || response.items.length === 0) {
                    console.log("No matching pages found");
                }
                else {
                    $("#resultsCount").html(response.searchInformation.formattedTotalResults + " results");
                    /* Navigation Links handling */
                    var totalItems = response.searchInformation.totalResults;
                    var totalListPages = totalItems / resultsPerPage;
                    var indexCount = response.queries.request[0].startIndex;
                    if (indexCount === totalListPages) {
                        //alert("stop next nav");
                    }
                    else if (indexCount === 1) {
                        //alert("stop prev nav");
                    }
                    $("#pageNumber").val(indexCount);
                    $("#totalListPages").html("of " + totalListPages);
                    $(".pagination-grid").show();
                    //console.log(totalListPages);
                    // loop handling
                    for (var i = 0; i < response.items.length; i++) {
                        var item = response.items[i];
                        var responseTitle = item.htmlTitle;
                        var responseUrl = item.htmlFormattedUrl;
                        var responseData = item.htmlSnippet;
                        html += "<div class='result-item'><a href='" + item.link + "' class='link-btn link-lg roboto-medium font-f'>" + responseTitle + "</a><a href='" + responseUrl + "' class='link-btn link-lg roboto-light font-h result-link'>" + responseUrl + "</a><p class='roboto-light font-h'>" + responseData + "</p></div>";
                    }
                    $("#resultContent").html(html);
                }
            }
        }
        
        /* Trim Url data to get search string */
        function getParameterByName(name, url) {
        	if (!url) url = window.location.href;
        	name = name.replace(/[\[\]]/g, "\\$&");
        	var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        		results = regex.exec(url);
        	if (!results) return null;
        	if (!results[2]) return '';
        	return decodeURIComponent(results[2].replace(/\+/g, " "));
        }