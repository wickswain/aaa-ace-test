$j(function($) {
    var searchMetaName = "";
    var searchMetaTag = "";
    var searchTotalListPages;
    var searchkeyValue = "";
    var searchResultsPerPage = 10;
    var searchPageNumber = 1;
    //hide everything on page load
    $("#search-error-message, #search-results-grid, #search-result-box, #search-pagination-grid, #resultsCount").hide();
    
    $("#search-categorie-block > li > a").click(function (e) {
        var searchMetaName = $(this).attr('data-search-meta-name');
        var searchMetaTag = $(this).attr('data-search-meta-content');
        localStorage.setItem("searchMetaName", searchMetaName);
        localStorage.setItem("searchMetaTag", searchMetaTag);
        $("#search-categorie-block > li > a").removeClass("active");
        $(this).addClass("active");
        var currentListItem = $(this).text();
        $("#search-checked-list-item-main").html(currentListItem);
        searchPageNumber = 1;
        getResultData(searchkeyValue, '', searchPageNumber);
        e.preventDefault();
    });
    $("#search-close-fliter-icon").click(function (e) {
        $('#search-filter-grid').hide();
        e.preventDefault();
    });
    $('#search-filter-link').click(function (e) {
        $("#search-filter-grid").show();
        e.preventDefault();
    });
    $(window).resize(function () {
        var winWidth = $(window).width();
        if (winWidth >= 768) {
            $("#search-filter-grid").show();
        }
    });
    
    /* if search is from header */
    var searchQuery = getParameterByName('q');
    if (searchQuery) {
        searchkeyValue = searchQuery;
        getResultData(searchkeyValue, '', 1);
        $("#searchKey").val(searchkeyValue);
    }
    
    $("#searchSubmit").click(function () {
        searchkeyValue = $("#searchKey").val();
        if (searchkeyValue) {
            localStorage.setItem("searchMetaName", '');
            localStorage.setItem("searchMetaTag", '');
            $("#search-categorie-block > li > a").removeClass("active");
            $("[data-search-meta-name='']").addClass("active");
            getResultData(searchkeyValue, '', 1);
        }
        else {
            /* Handle scenarios when after one search user search without input */
            $("#search-pagination-grid, #page_navigation, #search-results-grid, #resultsCount").hide();
            $("#search-error-message").html("No search results found.");
            $("#search-error-message").show();
            searchPageNumber = 1;
        }
    });
    //onEnter search event
    $("#searchKey").keyup(function (event) {
        if (event.keyCode == 13) {
            $('#searchSubmit').click();
        }
    });
    // Api Call based on page number
    function getResultData(searchkeyValue, navigation, currentPage) {
        if (navigation === -1) {
            searchPageNumber = parseInt(searchPageNumber) - 1;
        }
        if (navigation === 1) {
            searchPageNumber = parseInt(searchPageNumber) + 1;
        }
        if (currentPage) {
            searchPageNumber = currentPage;
        }
        var validPage = 0;
        validPage = (parseInt(searchPageNumber) * 10) + 1;
        validPage = validPage - 10;
        
        var apikey = $("#googleAPIKey").val();
        var engineid = $("#googleSearchEngineId").val();
 
        // restrict search to a region
        var domain = getDomain();
 
        var filterString = getFilter();
 
        if (filterString) {
            searchkeyValue = searchkeyValue + filterString;
            console.log("Query " + searchkeyValue);
        }
        
        var url = "https://www.googleapis.com/customsearch/v1?key=" + apikey + "&cx=" + engineid + "&q=" + searchkeyValue + "&start=" + validPage + "&siteSearch=" + domain + "&callback=?";
        
        $.getJSON(url, '', SearchCompleted);
    }
    // Final result appending to our html
    function SearchCompleted(response) {
    	$("#search-results-grid").show();
        if (response.error) {
            var errorMessage = response.error.message;
            $("#search-pagination-grid, #dataGrid, #resultsCount").hide();
            $("#search-error-message").show();
            $("#search-error-message").html("Sorry, something went wrong, if issue persist, please contact AAA.");
            searchPageNumber = 1;
        }
        else if (response.items == null || response.items.length === 0) {
        	$("#search-pagination-grid, #resultsCount, #dataGrid").hide();
            manageGrid();
            $("#search-error-message").show();
            $("#search-error-message").html("No search results found.");
            searchPageNumber = 1;
        }
        else {
        	$("#resultsCount, #search-result-box, #dataGrid").show();
        	$("#search-error-message").hide();
            manageGrid();
            var html = "";
            $("#resultsCount").html(response.searchInformation.formattedTotalResults + " results");
            /* Navigation Links handling */
            var totalItems = response.searchInformation.totalResults;
            searchTotalListPages = Math.ceil(totalItems / searchResultsPerPage);
            var indexCount = response.queries.request[0].startIndex;
            if (searchTotalListPages > 1) {
                $("#totalListPages").html("of " + searchTotalListPages);
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
            searchPageNumber = parseInt(searchPageNumber);
            if (parseInt(searchPageNumber) < 5) {
                defaultPage = 1;
                if (searchTotalListPages < 5) {
                    defaultTotalPages = searchTotalListPages + 1;
                }
                else {
                    defaultTotalPages = 6;
                }
            }
            else if (parseInt(searchPageNumber) > 5) {
                defaultPage = parseInt(searchPageNumber) - 2;
                defaultTotalPages = parseInt(searchPageNumber) + 3;
                
                if(defaultTotalPages > parseInt(searchTotalListPages) + 1){
                	defaultTotalPages = searchTotalListPages + 1 ;
                	defaultPage = searchTotalListPages - 4;
                }
            }

            
            var navigation_html = '<ul class="row"><li class="page-links col-xs-3"><a href="#" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="searchPrevPage"><span aria-hidden="true" class="prev-page glyphicon glyphicon-arrow-left"></span>Prev</a></li><li class="page-links text-center col-xs-6">';
            for (var i = defaultPage; i < defaultTotalPages; i++) {
                navigation_html += '<a href="javascript:void(0);" class="search-page-nav navPage_click" id="id' + i + '">' + (i) + '</a>';
            }
            navigation_html += '</li><li class="page-links text-right  col-xs-3"> <a href="#" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="searchNextPage">Next <span aria-hidden="true" class="next-page glyphicon glyphicon-arrow-right "></span></a></li></ul>';
            $('#search-pagination-grid').html(navigation_html);
            $('#search-pagination-grid').show();
            if (totalItems <= 10)
            {
            	$('#search-pagination-grid').hide();
            }
            $(".page_link").removeClass("active");
            $("#id" + searchPageNumber).addClass('active');
            $('#searchNextPage').click(function () {
                getResultData(searchkeyValue, 1, '');
                e.preventDefault();
            });
            $('#searchPrevPage').click(function () {
                getResultData(searchkeyValue, -1, '');
                e.preventDefault();
            });
            $(".search-page-nav").click(function () {
                var selectedPage = $(this).text();
                getResultData(searchkeyValue, '', selectedPage);
                e.preventDefault();
            });
            if (searchPageNumber === 1) {
                $("#searchPrevPage").addClass('disabled');
            }
            else if (searchPageNumber === searchTotalListPages) {
                $("#searchNextPage").addClass('disabled');
            }
            else {
                $("#searchNextPage").removeClass('disabled');
                $("#searchPrevPage").removeClass('disabled');
            }
            $("#search-resultContent").html(html);
        }
    }
    
    function manageGrid() {
        var winWidth = $(window).width();
        if (winWidth < 768) {
            $("#search-filter-grid").hide();
        }
        else {
            $("#search-filter-grid").show();
        }
    }
    
    $(window).resize(function () {
        manageGrid();
    });
	
    /* Trim Url data to get search string */
    function getParameterByName(name, url) {
        if (!url)
            url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"), results = regex
                .exec(url);
        if (!results)
            return null;
        if (!results[2])
            return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }
 
    /* Return domain to restrict region based search */
    function getDomain() {
        var hostname = window.location.hostname;
 
        // assuming the URL pattern of www.region.aaa.com
        var domain = hostname
                .substr(hostname.indexOf(".") + 1, hostname.length);
 
        return domain;
    }
 
    /* Return filters if user has clicked on filters */
    function getFilter() {
        var metaName = localStorage.getItem("searchMetaName");
        var metaContent = localStorage.getItem("searchMetaTag");
 
        if (metaName && metaContent) {
            var filterPrefix = ' more:pagemap:metatags-';
            var filterString = filterPrefix + metaName + ":" + metaContent;
            return filterString;
        }
 
        // return blank if no meta name or content found
        return;
    }
    
});
