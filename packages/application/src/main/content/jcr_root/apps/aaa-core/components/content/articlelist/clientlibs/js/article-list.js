/**
 * JS used in article hub page for article list component
 */

//Article List JS
$j(function ($) {
        var filterName = "";
        var filterTag = "";
        var articleTotalListPages;
        var articleResultsPerPage  = 10;
        var articlePageNumber = 1;
        $("#article-filter-link").click(function () {
            $("#article-filter-grid").show();
        });
        
		localStorage.setItem("filterName", '');
        localStorage.setItem("filterTag", '');
        
        getArticles('', articlePageNumber);
        
        //Filter click event
        $("#articleHub-categorie-block > li > a").click(function (e) {
            var filterName = $(this).attr('data-filter-name');
            var filterTag = $(this).attr('data-filter-tag');
            localStorage.setItem("filterName", filterName);
            localStorage.setItem("filterTag", filterTag);
            $("#articleHub-categorie-block > li > a").removeClass("active");
            $(this).addClass("active");
            var currentListItem = $(this).text();
            $("#article-checked-list-item-main").html(currentListItem);
            articlePageNumber = 1;
            getArticles('', articlePageNumber);
            e.preventDefault();
        });
        $("#article-close-fliter-icon").click(function (e) {
            $('#article-filter-grid').hide();
            e.preventDefault();
        });

        function manageArticleGrid() {
            var winWidth = $(window).width();
            if (winWidth < 768) {
                $("#article-filter-grid").hide();
            }
            else {
                $("#article-filter-grid").show();
            }
        }
        
        $(window).resize(function () {
            manageArticleGrid();
        });            
        
        /* API CALL */
        function getArticles(navigation, selectedPage) {
            
        	//user navigating through previous button
        	if (navigation === -1) {
                articlePageNumber = parseInt(articlePageNumber) - 1;
            }
        	
        	//user navigating through next button
            if (navigation === 1) {
                articlePageNumber = parseInt(articlePageNumber) + 1;
            }
            
            //if user has clicked a page number
            if (selectedPage) {
                articlePageNumber = selectedPage;
            }
            
            //calculate start index of results, it would be 11 for 2nd page, 21 for 3rd page and so on.
            var start = "";
            start = (parseInt(articlePageNumber) * 10) - 9;
            
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
                $("#article-hub, #pagination-grid").hide();
                $("#article-error-message").show();
                $("#article-error-message").html(errorMessage);
                articlePageNumber = 1;
            }
            else if (response.items == null || response.items.length === 0) {
                $("#article-hub, #pagination-grid").hide();
                $("#article-error-message").show();
                $("#article-error-message").html("No articles found.");
                manageArticleGrid();
                articlePageNumber = 1;
            }
            else {
            	manageArticleGrid();
                $("#article-error-message").hide();
                $("#article-hub").show();
                var html = "";
                
                /* Navigation Links handling */
                var totalItems = response.searchInformation.totalResults;
                articleTotalListPages = Math.ceil(totalItems / articleResultsPerPage );
                var indexCount = response.searchInformation.startIndex;
                if (articleTotalListPages > 1) {
                    $("#totalListPages").html("of " + articleTotalListPages);
                }
                // loop handling
                for (var i = 0; i < response.items.length; i++) {
                    var item = response.items[i];
                    
                    /* Declaring the div structure here. 
                     * Tried using html template tag but it has problem with IE.
                     * Putting this in html also creates issue since image will 404.
                     * Also due to pagination it looks tricky to keep final HTML clean. 	
                     */ 
                    
                    var listHtml = "<div id='article-list' class='row article-list with-img roboto-medium font-a'> " +
                    		"<div class='col-sm-12 col-md-4'>  <div class='imgblock'> " +
                    		"<img src='@articleImage@' alt='@articleImageAltText@' title='@articleImageAltText@' />  " +
                    		"</div> </div> <div class='col-sm-12 col-md-8 '>  " +
                    		"<div class='article-info'> <ul class='list-inline font-e'> " +
                    		"<li><img src='@articleLogoImage@' alt='@articleLogoAltText@' /></li> " +
                    		"<li class='author'>By @authorName@</li>" +
                    		"<li class='time'><i class='glyphicon glyphicon-time'></i><span class='date'> @issueDate@</span></li>" +
                    		"</ul>" +
                    		"<a href='@link@' class='roboto-slab-light font-b list-title' title='@articleTitle@'>@articleTitle@</a> " +
                    		"<p class='font-h roboto-light'>@description@</p><div class='list-btn visible-xs'>" +
                    		"<a href='@link@' title='Solid button' class='btn-style btn-lg btn-color-blue'>Read</a></div></div></div></div>";
                    
                    listHtml = listHtml.replace(/@articleTitle@/g, ((item.articleTitle) ? item.articleTitle : ""));
                    listHtml = listHtml.replace(/@link@/g, ((item.link) ? item.link : "#"));
                    
                    
                    //if author name is absent we have to remove "By" also.
                    if(!item.authorName || item.authorName === '')
                    {
                    	listHtml = listHtml.replace("<li class='author'>By @authorName@</li>", "");
                    }else
                    {
                    	listHtml = listHtml.replace(/@authorName@/g, item.authorName);
                    }

                    //if issue date is absent remove time icon as well
                    if(!item.issueDate || item.issueDate === '')
                    {
                    	listHtml = listHtml.replace("<li class='time'><i class='glyphicon glyphicon-time'></i><span class='date'> @issueDate@</span></li>", "");
                    }else
                    {
                    	listHtml = listHtml.replace(/@issueDate@/g, moment(item.issueDate, 'YYYY-MM-DD').locale("en").format('LL'));
                    }

                    listHtml = listHtml.replace(/@articleLogoImage@/g, ((item.articleLogoImage) ? item.articleLogoImage : ""));
                    listHtml = listHtml.replace(/@articleLogoAltText@/g, ((item.articleLogoAltText) ? item.articleLogoAltText : ""));
                    listHtml = listHtml.replace(/@description@/g, ((item.description) ? item.description : ""));
                    listHtml = listHtml.replace(/@articleImage@/g, ((item.articleImage) ? item.articleImage : ""));
                    listHtml = listHtml.replace(/@articleImageAltText@/g, ((item.articleImageAltText) ? item.articleImageAltText : ""));                    
                    
                    html += listHtml;
                }
                /* pagination Logic start */
                if (parseInt(articlePageNumber) < 5) {
                    defaultPage = 1;
                    if (articleTotalListPages < 5) {
                        defaultTotalPages = articleTotalListPages + 1;
                    }
                    else {
                        defaultTotalPages = 6;
                    }
                }
                else if (parseInt(articlePageNumber) > 5) {
                    defaultPage = parseInt(articlePageNumber) - 2;
                    defaultTotalPages = parseInt(articlePageNumber) + 3;
                    
                    if(defaultTotalPages > parseInt(articleTotalListPages) + 1){
                    	defaultTotalPages = articleTotalListPages + 1 ;
                    	defaultPage = articleTotalListPages - 4;
                    }
                    		
                    
                }
                
                
                /* Declaring the div structure here. 
                 * Tried using html template tag but it has problem with IE.
                 * Putting this in html also creates issue since image will 404.
                 * Also due to pagination it looks tricky to keep final HTML clean. 	
                 */ 
                var navigation_html = '<ul class="row list-inline"><li class="page-links col-xs-3"><a href="javascript:void(0);" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="articlePrevPage"><span aria-hidden="true" class="prev-page glyphicon glyphicon-arrow-left"></span> Prev</a></li><li class="page-links text-center col-xs-6">';
                for (var i = defaultPage; i < defaultTotalPages; i++) {
                    navigation_html += '<a href="javascript:void(0);" class="article_navPage_click navPage_click" id="articleid' + i + '">' + (i) + '</a>';
                }
                navigation_html += '</li><li class="page-links text-right  col-xs-3"> <a href="javascript:void(0);" title="Solid button" class="btn btn-style btn-sm btn-color-blue btn-reversed" id="articleNextPage">Next <span aria-hidden="true" class="next-page glyphicon glyphicon-arrow-right "></span></a></li></ul>';
                $('#pagination-grid').html(navigation_html);
                $('#pagination-grid').show();
                if (totalItems <= 10)
                {
                	$('#pagination-grid').hide();
                }
                $(".page-links").removeClass("active");
                $("#articleid" + articlePageNumber).addClass('active');
                $('#articleNextPage').click(function (event) {
                    getArticles(1, '');
                    event.preventDefault();
                });
                $('#articlePrevPage').click(function (event) {
                    getArticles(-1, '');
                    event.preventDefault();
                });
                $(".article_navPage_click").click(function () {
                    var selectedPage = $(this).text();
                    getArticles('', selectedPage);
                });
                if (parseInt(articlePageNumber) === 1) {
                    $("#articlePrevPage").addClass('disabled');
                }
                else if (parseInt(articlePageNumber) === articleTotalListPages) {
                    $("#articleNextPage").addClass('disabled');
                }
                else {
                    $("#articleNextPage").removeClass('disabled');
                    $("#articlePrevPage").removeClass('disabled');
                }
                $("#article-hub").html(html);
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
            
            return window.location.protocol+"//"+host;
        }
        
    });