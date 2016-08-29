        $(".close-fliter").click(function () {
            $(".mobile-filter-list").hide();
        });
        $('.mobile-filter-grid').hide();
        $(".mobile-filter-list").hide();
        $('.mobile-filter-grid').click(function () {
            $(".mobile-filter-list").show();
        });

        var searchkeyValue = "";
        $(".pagination-grid").hide();
        $(".results-grid").hide();

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
								getResultData(searchkeyValue, 0);
								//window.location.href = 'gsearch.html?searchKeyValue=' + searckData;
								//window.location.href = $("#result-page").val() + '.html' + '?q=' + searchkeyValue;
							} else {
								$(".error-message").html("No search results found.");
							}
						});
				
				//onEnter search event
				$("#searchKey").keyup(function(event) {
					if (event.keyCode == 13) {
						$('#searchSubmit').click();
					}
				});//end of key up

/* Search result */



//var searchkeyValue = getParameterByName('q');
var test = '${properties.jcr:title}';

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

        /*if(searchkeyValue){
            getResultData(searchkeyValue,0);
        }*/
        
        //Filter List click
        $(".meta-data").click(function () {
            var metaName = $(this).attr('data-metaname');
            var metaContent = $(this).attr('data-metacontent');
            localStorage.setItem("metaName", metaName);
            localStorage.setItem("metaContent", metaContent);
            $(".meta-data").removeClass("active");
            $(this).addClass("active");
            getResultData(searchkeyValue, pageNumber);
        });
        
        $(".mobile-list").click(function (e) {
            $(".mobile-list").removeClass("active");
            $(this).addClass("active");
            
            var metaName = $(this).attr('data-metaname');
            var metaContent = $(this).attr('data-metacontent');
            localStorage.setItem("metaName", metaName);
            localStorage.setItem("metaContent", metaContent);
            
            getResultData(searchkeyValue, pageNumber);
        });

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
            
            //AAA keys
            var apikey = 'AIzaSyBC-G6Eu9j36HTIkkCcw92_CVg_ONtK3Ec' ;//'AIzaSyA5Q7Z1wbPOUa59IhoZYO52ulQjWAndn3I';
            var engineid = '002841571323135263873:_kstmmls-cu'; //'004650686028114475301:tlpfyltlij4';
            
            //Axis 41
            //var apikey = 'AIzaSyA5Q7Z1wbPOUa59IhoZYO52ulQjWAndn3I';
            //var engineid = '004650686028114475301:tlpfyltlij4';

            
            var hostname = window.location.hostname;

            var domain = hostname.substr(hostname.indexOf(".")+1,hostname.length)
            
            var filterPrefix = ' more:pagemap:metatags-'; //DC.Language:en

            var metaName = localStorage.getItem("metaName");
            var metaContent = localStorage.getItem("metaContent");

            console.log(metaName);
            console.log(metaContent);
            
            if(metaName && metaContent)
            {
                var filterString = filterPrefix + metaName + ":" + metaContent;
                searchkeyValue = searchkeyValue + filterString;
            	console.log("Query " +searchkeyValue);
            }
            
            var url = "https://www.googleapis.com/customsearch/v1?key="+apikey+"&cx="+ engineid 
            			+ "&q=" + searchkeyValue + "&start=" + pageNumber 
            			+ "&siteSearch="+domain + "&callback=?";
            //+ "&siteSearch="+domain
            $.getJSON(url, '', SearchCompleted);
        }
        
        
        // Final result appending to our html
        function SearchCompleted(response) {
            if (response.error) {
                var errorMessage = response.error.message;
                $(".pagination-grid").hide();
                $(".results-grid").hide();
                $(".result-no").hide();
                $(".mobile-filter-list").hide();
                $('.mobile-filter-grid').hide();
                $(".error-message").show();
                $(".error-message").html(errorMessage);
                pageNumber = 1;
            }
            else if (response.items == null || response.items.length === 0) {
                $(".pagination-grid").hide();
                $(".results-grid").hide();
                $(".result-no").hide();
                $(".mobile-filter-list").hide();
                $('.mobile-filter-grid').hide();
                $(".error-message").show();
                $(".error-message").html("No search results found.");
                pageNumber = 1;
            }
            else {
                $(".error-message").hide();
                $(".pagination-grid").show();
                $(".results-grid").show();
                $(".result-no").show();
                //$(".mobile-filter-list").show();
                $('.mobile-filter-grid').show();
                var html = "";
                $("#resultsCount").html(response.searchInformation.formattedTotalResults + " results");
                $("#resultsCount-m").html(response.searchInformation.formattedTotalResults + " results");
                /* Navigation Links handling */
                var totalItems = response.searchInformation.totalResults;
                var totalListPages = Math.ceil(totalItems / resultsPerPage);
                var indexCount = response.queries.request[0].startIndex;
                if (indexCount === totalListPages) {
                    $("#nextPage").addClass('disabled');
                }
                else if (indexCount === 1) {
                    $("#prevPage").addClass('disabled');
                }
                else {
                    $("#nextPage").removeClass('disabled');
                    $("#prevPage").removeClass('disabled');
                }
                $("#pageNumber").val(indexCount);
                $("#totalListPages").html("of " + totalListPages);
                // loop handling
                for (var i = 0; i < response.items.length; i++) {
                    var item = response.items[i];
                    var responseTitle = item.htmlTitle;
                    var responseUrl = item.htmlFormattedUrl;
                    var responseData = item.htmlSnippet;
                    html += "<div class='result-item'><a href='" + item.link + "' class='link-btn link-lg roboto-medium font-f'>" + responseTitle + "</a><a href='" + item.link + "' class='link-btn link-lg roboto-light font-h result-link'>" + responseUrl + "</a><p class='roboto-light font-h'>" + responseData + "</p></div>";
                }
                $(".resultContent").html(html);
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