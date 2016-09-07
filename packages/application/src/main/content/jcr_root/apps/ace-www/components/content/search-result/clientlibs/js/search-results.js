        var searchkeyValue = "";
        var resultsPerPage = 10;
        var pageNumber = 1;
        
        var metaName = "";
        var metaContent = "";
        var totalListPages;
        
        //page load events, clear everything 
        $(".results-grid").hide();
        $(".error-message").hide();
        $('.mobile-filter-grid').hide();
        $(".mobile-filter-list").hide();
        $(".pagination-grid").hide();
        $("#page_navigation").hide();
        
        //register click events
        $('.mobile-filter-grid').click(function () {
            $(".mobile-filter-list").show();
        });
        $(".close-fliter-icon").click(function () {
            $(".mobile-filter-list").hide();
        });

		//onClick search event
		$("#searchSubmit").click(
				function() {
					searchkeyValue = $("#searchKey").val();
			        
					localStorage.setItem("metaName", '');
			        localStorage.setItem("metaContent", '');
					
			        if (searchkeyValue) {
			        	getResultData(searchkeyValue, '', 1, '');
					} else {
                        /* Handle scenarios when after one search user search without input */
                        $(".pagination-grid").hide();
                        $("#page_navigation").hide();
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

		
		//Default Pagination Config details
        $('#prevPage').click(function () {
            getResultData(searchkeyValue, -1);
        });
        $('#nextPage').click(function () {
            getResultData(searchkeyValue, 1);
        });

        /* Links disables handling */
        //Filter List click
        $(".meta-data").click(function () {
            var metaName = $(this).attr('data-metaname');
            var metaContent = $(this).attr('data-metacontent');
            localStorage.setItem("metaName", metaName);
            localStorage.setItem("metaContent", metaContent);
            $(".meta-data").removeClass("active");
            
            if(metaName)
            {
            	//if meta name is present, then its filter
            	$("[data-metaName=" + metaName + "]").addClass("active");
            }else
            {
            	//else it is all results
            	$(".all-results").addClass("active");
            }
            
            var currentListItem = $(this).text();
            $(".checked-list-item-main").html(currentListItem);
            $(".mobile-filter-list").hide();
            
            getResultData(searchkeyValue, '', 1);
        });
        
        
        // API Call based on page number
        function getResultData(searchkeyValue, navigation, currentPage) {
            if (navigation === -1) {
                pageNumber = parseInt(pageNumber) - 1;
            }
            if (navigation === 1) {
                pageNumber = parseInt(pageNumber) + 1;
            }
            if (currentPage) {
                pageNumber = currentPage;
            }
 
            var startIndex = ""
            startIndex = (pageNumber * 10) + 1;
            startIndex = startIndex - 10;
            
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
            			+ "&q=" + searchkeyValue + "&start=" + startIndex 
            			+ "&siteSearch="+domain + "&callback=?";
            
            /* Ajax call to Search API */
            $.getJSON(url, '', SearchCompleted);
        }
        
        
        // Final result appending to our html
        function SearchCompleted(response) {
            if (response.error) {
                var errorMessage = response.error.message;
                $(".pagination-grid").hide();
                $("#page_navigation").hide();
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
                $("#page_navigation").hide();
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
                $("#page_navigation").show();
                $(".results-grid").show();
                $(".result-no").show();
                $('.mobile-filter-grid').show();
                var html = "";
                $("#resultsCount").html(response.searchInformation.formattedTotalResults + " results");
                $("#resultsCount-m").html(response.searchInformation.formattedTotalResults + " results");
                /* Navigation Links handling */
                var totalItems = response.searchInformation.totalResults;
                var totalListPages = Math.ceil(totalItems / resultsPerPage);
                var indexCount = response.queries.request[0].startIndex;
                if (totalListPages > 1) {
                    $("#totalListPages").html("of " + totalListPages);
                }
                // loop handling
                for (var i = 0; i < response.items.length; i++) {
                    var item = response.items[i];
                    var responseTitle = item.htmlTitle;
                    var responseUrl = item.htmlFormattedUrl;
                    var responseData = item.snippet;
                    html += "<div class='result-item'><a href='" + item.link + "' class='link-btn link-lg roboto-medium font-f'>" + responseTitle + "</a><a href='" + item.link + "' class='link-btn link-lg roboto-light font-h result-link'>" + responseUrl + "</a><p class='roboto-light font-h'>" + responseData + "</p></div>";
                }
                /* pagination Logic start */
                if (parseInt(pageNumber) < 5) {
                    defaultPage = 1;
                    if (totalListPages < 5) {
                        defaultTotalPages = totalListPages;
                    }
                    else {
                        defaultTotalPages = 6;
                    }
                }
                else if (parseInt(pageNumber) > 5) {
                    defaultPage = parseInt(pageNumber) - 2;
                    defaultTotalPages = parseInt(pageNumber) + 3;
                }

                var navigation_html = '<div class="row pagination-grid  horizontal-component-space "><ul>';
                navigation_html += '<li class="page-links col-md-4 col-sm-4 col-xs-4">';
                navigation_html += '<a href="javascript:void(0);" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="prevPage"><span aria-hidden="true" class="prev-page glyphicon glyphicon-arrow-left"></span>Prev</a>';
                navigation_html += '</li>';
                for (var i = defaultPage; i < defaultTotalPages; i++) {
                    navigation_html += '<li class="page_link" id="id' + i + '">';
                    navigation_html += '<a href="javascript:void(0);" class="pagination-pageclick">' + (i) + '</a>';
                    navigation_html += '</li>';
                }
 
                navigation_html += '<li class="page-links col-md-4 col-sm-4 col-xs-4 pull-right">';
                navigation_html += '<a href="javascript:void(0);" title="Solid button" class="btn pull-right  btn-style btn-sm btn-color-blue btn-reversed" id="nextPage">Next<span aria-hidden="true" class="next-page glyphicon glyphicon-arrow-right "></span></a>';
                navigation_html += '</li>';
                navigation_html += '</ul><div>';
                
                $('#page_navigation').html(navigation_html);
                $(".page_link").removeClass("active");
                $("#id" + pageNumber).addClass('active');
                $('#nextPage').click(function () {
                	getResultData(searchkeyValue, 1, '');
                });
                $('#prevPage').click(function () {
                	getResultData(searchkeyValue, -1, '');
                });
                
                $(".pagination-pageclick").click(function () {
                    var selectedPage = $(this).text();

                    getResultData(searchkeyValue, '', selectedPage);
                });
                if (pageNumber === 1) {
                    $("#prevPage").addClass('disabled');
                }
                else if (pageNumber === totalListPages) {
                    $("#nextPage").addClass('disabled');
                }
                else {
                    $("#nextPage").removeClass('disabled');
                    $("#prevPage").removeClass('disabled');
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