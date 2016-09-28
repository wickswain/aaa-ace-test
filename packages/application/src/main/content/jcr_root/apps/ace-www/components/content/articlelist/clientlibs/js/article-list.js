/**
 * JS used in article hub page for article list component
 */

//Article List JS
$(function () {
        var filterName = "";
        var filterTag = "";
        var totalListPages;
        var resultsPerPage = 10;
        var pageNumber = 1;
        $(".filter-link").click(function () {
            $(".filter-grid").show();
        });
        
		localStorage.setItem("filterName", '');
        localStorage.setItem("filterTag", '');
        
        getArticles('', pageNumber);
        
        //Filter click event
        $(".categorie-block li a").click(function (e) {
            var filterName = $(this).attr('filterName');
            var filterTag = $(this).attr('data-filter-tag');
            localStorage.setItem("filterName", filterName);
            localStorage.setItem("filterTag", filterTag);
            $(".categorie-block li a").removeClass("active");
            $(this).addClass("active");
            var currentListItem = $(this).text();
            $(".checked-list-item-main").html(currentListItem);
            pageNumber = 1;
            getArticles('', pageNumber);
            
            e.preventDefault();
        });
        $(".close-fliter-icon").click(function (e) {
            $('.filter-grid').hide();
            e.preventDefault();
        });
        $(window).resize(function () {
            var winWidth = $(window).width();
            if (winWidth >= 768) {
                $(".filter-grid").show();
            }
        });
        /* API CALl */
        function getArticles(navigation, selectedPage) {
            
        	//user navigating through previous button
        	if (navigation === -1) {
                pageNumber = parseInt(pageNumber) - 1;
            }
        	
        	//user navigating through next button
            if (navigation === 1) {
                pageNumber = parseInt(pageNumber) + 1;
            }
            
            //if user has clicked a page number
            if (selectedPage) {
                pageNumber = selectedPage;
            }
            
            //calculate start index of results, it would be 11 for 2nd page, 21 for 3rd page and so on.
            var start = "";
            start = (parseInt(pageNumber) * 10) - 9;
            
            var articleFilterTagLocal = localStorage.getItem("filterTag");
            var filter = "";
            if(articleFilterTagLocal)
            {
            	filter = "&filter=" + articleFilterTagLocal;
            }
            
            var url = getHost() + "/get.articles.json?start="+start+filter;
            $.getJSON(url, '', articleReceived);
        }

        function articleReceived(response) {
            if (response.error) {
                var errorMessage = response.error.message;
                $(".article-hub").hide();
                //$(".result-no").hide();
                $(".error-grid").show();
                $(".error-message").html(errorMessage);
                pageNumber = 1;
            }
            else if (response.items == null || response.items.length === 0) {
                $(".article-hub").hide();
                //$(".result-no").hide();
                $(".error-grid").show();
                $(".error-message").html("No search results found.");
                pageNumber = 1;
            }
            else {
                $(".error-grid").hide();
                $(".article-hub").show();
                var html = "";
                /* Navigation Links handling */
                var totalItems = response.searchInformation.totalResults;
                totalListPages = Math.ceil(totalItems / resultsPerPage);
                var indexCount = response.searchInformation.startIndex;
                if (totalListPages > 1) {
                    $("#totalListPages").html("of " + totalListPages);
                }
                // loop handling
                for (var i = 0; i < response.items.length; i++) {
                    var item = response.items[i];
                    
                    /* Declaring the div structure here. 
                     * Tried using html template tag but it has problem with IE.
                     * Putting this in html also creates issue since image will 404.
                     * Also due to pagination it looks tricky to keep final HTML clean. 	
                     */ 
                    
                    var listHtml = "<div id='article-list' class='row article-list with-img roboto-medium font-a'> <div class='col-sm-12 col-md-6 col-lg-4'>  <div class='imgblock'> <img src='@articleImage@' alt='@articleImageAltText@' title='@articleImageAltText@' />  </div> </div> <div class='col-sm-12 col-md-6 col-lg-8 '>  <div class='article-info'> <ul class='list-inline font-e'> <li><img src='@articleLogoImage@' alt='@articleLogoAltText@' /></li> <li class='author'>By @authorName@</li> <li class='time'><i class='glyphicon glyphicon-time'></i> <span class='date'>@issueDate@</span></li> </ul> <a href='@link@' class='roboto-light font-b list-title' title='@articleTitle@'>@articleTitle@</a> <p class='font-h roboto-light'>@description@</p><div class='list-btn visible-xs'><a href='@link@' title='Solid button' class='btn-style btn-lg btn-color-blue'>Read</a></div>  </div> </div> </div>";
                    
                    listHtml = listHtml.replace(/@articleTitle@/g, ((item.articleTitle) ? item.articleTitle : ""));
                    listHtml = listHtml.replace(/@link@/g, ((item.link) ? item.link : "#"));
                    listHtml = listHtml.replace(/@authorName@/g, ((item.authorName) ? item.authorName : ""));
                    listHtml = listHtml.replace(/@articleLogoImage@/g, ((item.articleLogoImage) ? item.articleLogoImage : ""));
                    listHtml = listHtml.replace(/@issueDate@/g, ((item.issueDate) ? item.issueDate : ""));
                    listHtml = listHtml.replace(/@articleLogoAltText@/g, ((item.articleLogoAltText) ? item.articleLogoAltText : ""));
                    listHtml = listHtml.replace(/@description@/g, ((item.description) ? item.description : ""));
                    listHtml = listHtml.replace(/@articleImage@/g, ((item.articleImage) ? item.articleImage : ""));
                    listHtml = listHtml.replace(/@articleImageAltText@/g, ((item.articleImageAltText) ? item.articleImageAltText : ""));                    
                    
                    html += listHtml;
                }
                /* pagination Logic start */
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
                
                
                /* Declaring the div structure here. 
                 * Tried using html template tag but it has problem with IE.
                 * Putting this in html also creates issue since image will 404.
                 * Also due to pagination it looks tricky to keep final HTML clean. 	
                 */ 
                var navigation_html = '<ul class="row list-inline"><li class="page-links col-xs-3"><a href="#" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="prevPage"><span aria-hidden="true" class="prev-page glyphicon glyphicon-arrow-left"></span> Prev</a></li><li class="page-links text-center col-xs-6">';
                for (var i = defaultPage; i < defaultTotalPages; i++) {
                    navigation_html += '<a href="javascript:void(0);" class="navPage_click" id="id' + i + '">' + (i) + '</a>';
                }
                navigation_html += '</li><li class="page-links text-right  col-xs-3"> <a href="#" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="nextPage">Next <span aria-hidden="true" class="next-page glyphicon glyphicon-arrow-right "></span></a></li></ul>';
                $('#pagination-grid').html(navigation_html);
                $('.pagination-grid').show();
                if (totalItems <= 10)
                {
                	$('.pagination-grid').hide();
                }
                $(".page_link").removeClass("active");
                $("#id" + pageNumber).addClass('active');
                $('#nextPage').click(function () {
                    getArticles(1, '');
                });
                $('#prevPage').click(function () {
                    getArticles(-1, '');
                });
                $(".navPage_click").click(function () {
                    var selectedPage = $(this).text();
                    getArticles('', selectedPage);
                });
                if (parseInt(pageNumber) === 1) {
                    $("#prevPage").addClass('disabled');
                }
                else if (parseInt(pageNumber) === totalListPages) {
                    $("#nextPage").addClass('disabled');
                }
                else {
                    $("#nextPage").removeClass('disabled');
                    $("#prevPage").removeClass('disabled');
                }
                $(".article-hub").html(html);
            }
        }
        
        
        /* Return domain to restrict region based search */
        function getHost()
        {
            var host = window.location.hostname;
            var port = window.location.port;
            
            if (port)
            {
            	host = host + ":" + port;
            }
            
            return "http://"+host
        }
        
    });