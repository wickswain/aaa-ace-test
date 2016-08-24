$("#paginationLinks").hide();
$("#error-alert").hide();

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

/* Pagination Controls */
var searchQuery = getParameterByName('searchKeyValue');	
var region = getParameterByName('region');

//console.log("====> region " + region)
var _prevIndex = 0;
var _nextIndex = 0;
var _resultsPerPage = 10;
var _pageNumber = 1;

/* Next and previuos page actions */
$(function ()
{
	$('#lnkPrev').click(function () { Search(searchQuery,-1); });
	$('#lnkNext').click(function () { Search(searchQuery,1);  });
});

if(searchQuery){
	Search(searchQuery,0);
}	


/* Manage Api Calls Based on pagenumber dynamically */
function Search(term, direction)
{
	var startIndex = 1;						

	if (direction === -1)
	{
		startIndex = _prevIndex; 
		_pageNumber--;
	}
	if (direction === 1)
	{
		startIndex = _nextIndex; 
		_pageNumber++;
	}
	if (direction === 0)
	{
		startIndex = 1; 
		_pageNumber = 1;
	}

	var siteSearch = $('#'+region).val();

	//console.log("cx: " + cx);

	var url ="https://www.googleapis.com/customsearch/v1?key=AIzaSyA5Q7Z1wbPOUa59IhoZYO52ulQjWAndn3I&cx=004650686028114475301:xbvdr_4yrom&q="+searchQuery+"&start="+startIndex+"&siteSearch="+siteSearch+"&callback=?";
	$(".pageloaddiv").show();
	$("#paginationLinks").hide();
	$("#error-alert").hide();
	$.getJSON(url, '', SearchCompleted);
}

/* Manage Response data and binding to result div */
function SearchCompleted(response)
{

	if(response.error){
		var errorMessage= response.error.errors[0].message;
		$("#error-alert").show();
			$("#error-alert").html(errorMessage);
			$("#paginationLinks").hide();
	}else{
	$("#error-alert").hide();
		var html = "";
		$("#searchResult").html("");

		if (response.items == null)
		{
			$("#searchResult").html("No matching pages found");
			return;
		}

		if (response.items.length === 0)
		{
			$("#searchResult").html("No matching pages found");
			return;
		}

		$("#searchResult").html(response.queries.request[0].totalResults + " pages found");

		if (response.queries.nextPage != null)
		{
			_nextIndex = response.queries.nextPage[0].startIndex;
			$("#lnkNext").show();
		}
		else
		{
			$("#lnkNext").hide();
		}

		if (response.queries.previousPage != null)
		{
			_prevIndex = response.queries.previousPage[0].startIndex;
			$("#lnkPrev").show();
		}
		else
		{
			$("#lnkPrev").hide();
		}

		if (response.queries.request[0].totalResults > _resultsPerPage)
		{
			$("#lblPageNumber").show().html(_pageNumber);
		}
		else
		{
			$("#lblPageNumber").hide();
		}
		for (var i = 0; i < response.items.length; i++)
		{
			var item = response.items[i];
			var title = item.htmlTitle;
			var icon = icon = "class='cmisc' title='Other page'";
			
			html += "<div><div class='searchDataHeading'><a " + icon + " href='" + item.link + "'> " + title + "</a></div>";
			html += "<div class='searchContent'>"+item.htmlSnippet + "</div></div><hr/>";
		}
		$(".pageloaddiv").hide();
		$("#paginationLinks").show();
		$("#resultContent").html(html);				
	}
}
