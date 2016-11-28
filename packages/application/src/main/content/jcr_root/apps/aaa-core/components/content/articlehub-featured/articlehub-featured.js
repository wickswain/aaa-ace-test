use(["/etc/clientlibs/granite/moment/source/moment.js"], 
    function (moment)
 {
    var formattedIssueDateLocal =  moment(this.issuedate, 'YYYY-MM-DD').locale("en").format('LL');
    

    return {
    	formattedIssueDate : formattedIssueDateLocal
    };
});