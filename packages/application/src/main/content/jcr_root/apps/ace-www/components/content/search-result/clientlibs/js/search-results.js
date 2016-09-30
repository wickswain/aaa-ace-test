$j(function($) {
    var metaName = "";
    var metaTag = "";
    var totalListPages;
    var searchkeyValue = "";
    var resultsPerPage = 10;
    var pageNumber = 1;
    $("#error-grid").hide();
    $(".error-message").hide();
    $(".results-grid").hide();
    $(".results-box").hide();
    $(".pagination-grid").hide();
    
    $(".categorie-block li a").click(function (e) {
        var metaName = $(this).attr('data-metaname');
        var metaTag = $(this).attr('data-metacontent');
        localStorage.setItem("metaName", metaName);
        localStorage.setItem("metaTag", metaTag);
        $(".categorie-block li a").removeClass("active");
        $(this).addClass("active");
        var currentListItem = $(this).text();
        $(".checked-list-item-main").html(currentListItem);
        pageNumber = 1;
        getResultData(searchkeyValue, '', pageNumber);
        e.preventDefault();
    });
    $(".close-fliter-icon").click(function (e) {
        $('.filter-grid').hide();
        e.preventDefault();
    });
    $('.filter-link').click(function (e) {
        $(".filter-grid").show();
        e.preventDefault();
    });
    $(window).resize(function () {
        var winWidth = $(window).width();
        if (winWidth >= 768) {
            $(".filter-grid").show();
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
            localStorage.setItem("metaName", '');
            localStorage.setItem("metaTag", '');
            $(".categorie-block li a").removeClass("active");
            $("[data-metaname='']").addClass("active");
            getResultData(searchkeyValue, '', 1);
        }
        else {
            /* Handle scenarios when after one search user search without input */
            $(".pagination-grid").hide();
            $("#page_navigation").hide();
            $(".results-grid").hide();
            $(".result-no").hide();
            $(".error-message").html("No search results found.");
            $("#error-grid").show();
            $(".error-message").show();
            pageNumber = 1;
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
            pageNumber = parseInt(pageNumber) - 1;
        }
        if (navigation === 1) {
            pageNumber = parseInt(pageNumber) + 1;
        }
        if (currentPage) {
            pageNumber = currentPage;
        }
        var validPage = 0;
        validPage = (parseInt(pageNumber) * 10) + 1;
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
    	$(".results-grid").show();
        if (response.error) {
            var errorMessage = response.error.message;
            $(".pagination-grid").hide();
            $("#dataGrid").hide();
            $(".result-no").hide();
            $(".error-message").show();
            $(".error-message").html("Sorry, something went wrong, if issue persist, please contact AAA.");
            pageNumber = 1;
        }
        else if (response.items == null || response.items.length === 0) {
        	$(".pagination-grid").hide();
            $(".result-no").hide();
            $("#dataGrid").hide();
            manageGrid();
            $(".error-message").show();
            $(".error-message").html("No search results found.");
            pageNumber = 1;
        }
        else {
            $(".error-message").hide();
            $(".result-no").show();
            $(".result-box").show();
            $("#dataGrid").show();
            manageGrid();
            var html = "";
            $(".result-no").html(response.searchInformation.formattedTotalResults + " results");
            /* Navigation Links handling */
            var totalItems = response.searchInformation.totalResults;
            totalListPages = Math.ceil(totalItems / resultsPerPage);
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
            pageNumber = parseInt(pageNumber);
            if (parseInt(pageNumber) < 5) {
                defaultPage = 1;
                if (totalListPages < 5) {
                    defaultTotalPages = totalListPages + 1;
                }
                else {
                    defaultTotalPages = 6;
                }
            }
            else if (parseInt(pageNumber) > 5) {
                defaultPage = parseInt(pageNumber) - 2;
                defaultTotalPages = parseInt(pageNumber) + 3;
                
                if(defaultTotalPages > parseInt(totalListPages) + 1){
                	defaultTotalPages = totalListPages + 1 ;
                	defaultPage = totalListPages - 4;
                }
            }

            
            var navigation_html = '<ul class="row"><li class="page-links col-xs-3"><a href="#" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="prevPage"><span aria-hidden="true" class="prev-page glyphicon glyphicon-arrow-left"></span>Prev</a></li><li class="page-links text-center col-xs-6">';
            for (var i = defaultPage; i < defaultTotalPages; i++) {
                navigation_html += '<a href="javascript:void(0);" class="navPage_click" id="id' + i + '">' + (i) + '</a>';
            }
            navigation_html += '</li><li class="page-links text-right  col-xs-3"> <a href="#" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="nextPage">Next <span aria-hidden="true" class="next-page glyphicon glyphicon-arrow-right "></span></a></li></ul>';
            $('.pagination-grid').html(navigation_html);
            $('.pagination-grid').show();
            if (totalItems <= 10)
            {
            	$('.pagination-grid').hide();
            }
            $(".page_link").removeClass("active");
            $("#id" + pageNumber).addClass('active');
            $('#nextPage').click(function () {
                getResultData(searchkeyValue, 1, '');
                e.preventDefault();
            });
            $('#prevPage').click(function () {
                getResultData(searchkeyValue, -1, '');
                e.preventDefault();
            });
            $(".navPage_click").click(function () {
                var selectedPage = $(this).text();
                getResultData(searchkeyValue, '', selectedPage);
                e.preventDefault();
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
    
    function manageGrid() {
        var winWidth = $(window).width();
        if (winWidth < 768) {
            $(".filter-grid").hide();
        }
        else {
            $(".filter-grid").show();
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
        var metaName = localStorage.getItem("metaName");
        var metaContent = localStorage.getItem("metaTag");
 
        if (metaName && metaContent) {
            var filterPrefix = ' more:pagemap:metatags-';
            var filterString = filterPrefix + metaName + ":" + metaContent;
            return filterString;
        }
 
        // return blank if no meta name or content found
        return;
    }
    
});
