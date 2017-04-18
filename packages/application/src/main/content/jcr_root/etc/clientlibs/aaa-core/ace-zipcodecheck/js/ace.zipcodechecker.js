
/*
 * Main entry point for the zip code router.
 * Call this function from your page to enable zip code checking.
 * 
 * Note: this assumes xxx.aaa.com  sub-domains...
 *
 */
function checkZip() {

	var qString = window.location.search;
	
	//check for crawler
	if (!isCrawler(navigator.userAgent))
	{
		//look for zipcode query string
		var qZip = getQueryStringValue('zip', window.location.search);
		var bypass = getQueryStringValue('bp', window.location.search);		
		var stopAtZip = getQueryStringValue('stop');
		var nationalUrl = 'http://www.aaa.com/?rclub=' + getReferringClub() + '&rurl=' + escape(window.location);
		var nationalStopUrl = 'http://www.aaa.com/stop?rclub=' + getReferringClub() + '&rurl=' + escape(window.location);;
				
		if (qZip != null)
		{
			//write ace zip
			writeACECookie(qZip);
		}//if (qZip != null)
		else if (validCookieExists())
		{
			//we have a valid zip code cookie so are good
			return;
		}//else if (getCookieValue('acezipcode') != null)
		else if (bypass == 1)
		{
			//used for internal testing
			return;
		}
		else
		{
			if(isInAuthor())
				return;
				
			if (stopAtZip == 1)
				window.location = nationalStopUrl;
			else
				window.location = nationalUrl;
		}//else if (nationalcookie == null && acecookie == null)
		
	}//if (!isCrawler(navigator.userAgent))
}//function CheckZip()

function isInAuthor()
{
	var rurl = window.location.toString().toLowerCase();
	var result = false;
	
	if(rurl != null)
	{
		if (rurl.indexOf('authcq') != -1)
			result = true;
	}//if(rurl != null)
	
	return result;
}//function isInAuthor()

/*
 * Get's the querystring value for a given parameter name
 *
 * Returns: 	The value for the query string parameter name
 * 				or null if there is no matching query string parameter name
 *
 */
function getQueryStringValue(qname, inQString) {
	var qstring = inQString;
	var result = null;
	var nvPairs;
	var nvItem;
	var itemName;
	var itemValue;

	if (inQString != null) {
		//grab the query string without the leading ?
		qstring = inQString.substring(1);

		if (qname != null)
			qname = qname.toLowerCase();

		if (qstring != "") {
			nvPairs = qstring.split("&");

			for (var idx = 0; idx < nvPairs.length; idx++) {
				nvItem = nvPairs[idx].split("=");

				itemName = nvItem[0].toLowerCase();
				itemValue = nvItem[1];

				if (itemName == qname) {
					result = unescape(itemValue);
					break;
				}//if (itemName == qname)
			}//for (var idx = 0; idx < nvPairs.length; idx++)
		}//if (qstring != '')
	}//if (inQString != null)

	return result;
}//function getQueryStringValue(qname)

/*
 * Determines if the requester is a search index crawler
 *
 * Returns true if the user agent matches an entry in our list of search agents
 */
function isCrawler(userAgent) {
	var result = false;
	var lowercaseAgent = null;

	if (userAgent != null) {
		lowercaseAgent = userAgent.toLowerCase();

		if (lowercaseAgent.indexOf('googlebot') != -1)
			return true;

		if (lowercaseAgent.indexOf('backrub') != -1)
			return true;

		if (lowercaseAgent.indexOf('msnbot') != -1)
			return true;

		if (lowercaseAgent.indexOf('yahoo') != -1)
			return true;

		if (lowercaseAgent.indexOf('lycos') != -1)
			return true;

		if (lowercaseAgent.indexOf('mercator') != -1)
			return true;

		if (lowercaseAgent.indexOf('keynote') != -1)
			return true;

		if (lowercaseAgent.indexOf('slurp') != -1)
			return true;
	}//if (userAgent != null)

	return false;
}//function isCrawler()

/*
 * Gets the name=value for the given cookie name
 */
function getCookie(cookieName) {
	var cName = null;
	var result = null;
	var cookieArray = document.cookie.split(";");

	for (var i = 0; i < cookieArray.length; i++) {
		cName = cookieArray[i].substr(0, cookieArray[i].indexOf("="));
		cName = cName.replace(/^\s+|\s+$/g, "");

		if (cName == cookieName) {
			result = unescape(cookieArray[i]);
		}
	}//for (var i=0; i< cookieArray.length; i++)

	return result;
}//function getCookie(cookieName)

/*
 * Gets the value portion for the given cookie name
 */
function getCookieValue(cookieName) {
	var result = null;
	var tmpCookie = null;
	
	tmpCookie = getCookie(cookieName);

	if (tmpCookie != null && tmpCookie.indexOf('=') > 0) {
		result = tmpCookie.split('=')[1];
	}//if (tmpCookie != null && tmpCookie.indexOf('=') > 0)

	return result;
}//funciton getCookieValue(cookieName)

/*
 * Writes the cookie
 */
function setCookie(cookieName, cookieValue, path, domain, daysTillExpire) {
	var theCookie;
	var expDate = new Date();
	var tmpValue = escape(cookieValue);

	theCookie = cookieName + '=' + tmpValue;

	//set the expriation date
	if (daysTillExpire != null && daysTillExpire != '')
		expDate.setDate(expDate.getDate() + daysTillExpire);
	else
		expDate.setDate(expDate.getDate() + 365);

	theCookie += "\; expires=" + expDate.toUTCString();

	//add the domain if supplied
	if (domain != null)
		theCookie += '; domain=' + domain;

	//add the path if supplied
	if (path != null)
		theCookie += '; path=' + path;

	//save the cookie
	document.cookie = theCookie;

}//function setCookie(cookieName, cookieValue, path)

/*
 * checks the format of the ACE zip code cookie
 */
function isValidACECookie()
{
	var result = false;
	var acecookie = getCookieValue('acezipcode');
	var ACE_ZIP_IDX = 2;
	var AAA_IDX = 1;
	
	if (acecookie != null)
	{
		var cookieparts = acecookie.split('|');
		
		if (cookieparts != null && cookieparts.length == 3)
		{
			result = ((cookieparts[ACE_ZIP_IDX].length == 5) && (cookieparts[AAA_IDX] == 'AAA'));
		}//if (parts != null && parts.length > 0)
	}//if (acecookie != null)
	
	return result;
}//function isValidACECookie()

/*
 * check the format of the AAA national zip code cookie
 */
function isValideNationalCookie()
{
	var result = false;
	var aaacookie = getCookieValue('zipcode');
	var AAA_IDX = 1;
	var NAT_ZIP_IDX = 0;
	
	if (aaacookie != null)
	{
		var cookieparts = aaacookie.split('|');
		
		if (cookieparts != null && cookieparts.length == 3)
		{
			result = ((cookieparts[NAT_ZIP_IDX].length == 5) && (cookieparts[AAA_IDX] == 'AAA'));
		}//if (parts != null && parts.length > 0)
	}//if (acecookie != null)
	
	return result;
	
}//function isValideNationalCookie(

function validCookieExists()
{
	var result = false;
	var cookieparts = null;

	var ACE_ZIP_IDX = 2;
	var AAA_IDX = 1;
	var NAT_ZIP_IDX = 0;

	
	//check for an ACE cookie	
	var acecookie = getCookieValue('acezipcode');
	
	if (acecookie != null)
	{
		cookieparts = acecookie.split('|');
		
		if (cookieparts != null && cookieparts.length == 3)
		{
			result = ((cookieparts[ACE_ZIP_IDX].length == 5) && (cookieparts[AAA_IDX] == 'AAA'));
		}//if (parts != null && parts.length > 0)
	}//if (acecookie != null)
	
	return result;
}//function validCookieExists()

/*
 * calculates the club code to be used for referring club
 */
function getReferringClub()
{
	var result = '';
	var rurl = window.location.toString().toLowerCase()
	
	if (rurl.indexOf('calif') != -1)
		result = '4';
	else if (rurl.indexOf('texas') != -1)
		result = '252';
	else if (rurl.indexOf('newmexico') != -1)
		result = '601';
	else if (rurl.indexOf('hawaii') != -1)
		result = '18';
	else if (rurl.indexOf('northernnewengland') != -1  || rurl.indexOf('nne') != -1)
		result = '36';
	else if (rurl.indexOf('alabama') != -1)
		result = '1';
	else if (rurl.indexOf('eastcentral') != -1)
		result = '215';
	else if (rurl.indexOf('tidewater') != -1)
		result = '258';
	else if (rurl.indexOf('autoclubmo') != -1)
		result = '65';
	else
		resutl = '4';
	
	return result;
}//function getReferringClub()


function writeACECookie(zipcode)
{
	var acecookie = getReferringClub() + '|AAA|' + zipcode;
	var domain = window.location.hostname;
	var front = null;
	
	var containsAAA = domain.indexOf('.aaa.com') != -1;
	
	if (containsAAA)
	{
		front = domain.substr(0, domain.indexOf('.aaa.com'));

		if (front != null && front.length > 0)
		{
			var idx = front.indexOf('.');
			
			if (idx != -1)
				front = front.substr(idx + 1)
			
			domain = '.' + front + '.aaa.com';
			
			setCookie('acezipcode', acecookie, '/', domain);
		}//if (front != null && front.length > 0)
	}//if (containsAAA)
	else
	{
		setCookie('acezipcode', acecookie, '/');	
	}
}//function writeACECookie(zipcode)