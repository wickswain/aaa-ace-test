(function (document, $, ns) {
"use strict";

	$(document).on("click", ".cq-dialog-submit", function (e) {
		var $form = $(this).closest("form.foundation-form"),
		isDateInvalid = $form.find("[name='./issueDate']").hasClass('is-invalid'),
        issueDate = $form.find("[name='./issueDate']").val(),
		message, clazz = "coral-Button ",
		dateMoment = moment(issueDate);

        if((issueDate != "" && issueDate != null && (!dateMoment.isValid() || dateMoment.year() < 1970 || dateMoment.year() > moment().year() ))
        	|| ((issueDate === "" || issueDate === null) && isDateInvalid)){
        	ns.ui.helpers.prompt({

                title: Granite.I18n.get("Invalid Input"),
                message:"<font color='red'>Please enter a valid date format (September 09, 16) .<br/> Note: year should be between 1970 and current year</font>",
        		actions: [{
                    id: "OK",
        			text: "OK",
        			className: "coral-Button"
        		}], callback: function (actionId) {
        				if (actionId === "OK") {
						// do nothing
                        }
        			}
        	}); //end prompt
        	 return false;
            }
         else{
			 return true;
        	$form.submit();
        }
	});
})(document, Granite.$, Granite.author);