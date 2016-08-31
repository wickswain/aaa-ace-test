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
                        /* Handle scenarios when after one search user search without input */
                        $(".pagination-grid").hide();
                        $(".results-grid").hide();
                        $(".result-no").hide();
                        $(".mobile-filter-list").hide();
                        $('.mobile-filter-grid').hide();
                        $(".error-message").show();
                        $(".error-message").html("No search results found.");
                        pageNumber = 1;
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
            getResultData(searchkeyValue, 0);
        });
        
        $(".mobile-list").click(function (e) {
            $(".mobile-list").removeClass("active");
            $(this).addClass("active");
            
            var metaName = $(this).attr('data-metaname');
            var metaContent = $(this).attr('data-metacontent');
            localStorage.setItem("metaName", metaName);
            localStorage.setItem("metaContent", metaContent);
            
            getResultData(searchkeyValue, 0);
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
            
            var apikey = $("#googleAPIKey").val();
            var engineid = $("#googleSearchEngineId").val();
            
            //restrict search to a region
            var domain = getDomain();
            
            var filterString = getFilter();
            
            if (filterString)
            {
                searchkeyValue = searchkeyValue + filterString;
            	console.log("Query " +searchkeyValue);
            }

            var url = "https://www.googleapis.com/customsearch/v1?key="+apikey+"&cx="+ engineid 
            			+ "&q=" + searchkeyValue + "&start=" + pageNumber 
            			+ "&siteSearch="+domain + "&callback=?";
            
            /* Ajax call to Search API */
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
        
        /* Return domain to restrict region based search */
        function getDomain()
        {
            var hostname = window.location.hostname;
            
            //assuming the URL pattern of www.region.aaa.com
            var domain = hostname.substr(hostname.indexOf(".")+1,hostname.length);
            
            return domain;
        }
        
        /* Return filters if user has clicked on filters */
        function getFilter()
        {
            var metaName = localStorage.getItem("metaName");
            var metaContent = localStorage.getItem("metaContent");

            if(metaName && metaContent)
            {
            	var filterPrefix = ' more:pagemap:metatags-'; 
            	var filterString = filterPrefix + metaName + ":" + metaContent;
            	return filterString;
            }
            
            //return blank if no meta name or content found
            return;
        }