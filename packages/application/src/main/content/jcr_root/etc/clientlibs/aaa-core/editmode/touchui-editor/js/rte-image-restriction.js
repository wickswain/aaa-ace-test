(function (document, $, ns) {
"use strict";
$(document).on("click", ".cq-dialog-submit", function (e) {
	e.stopPropagation();
	e.preventDefault();
	var $form = $(this).closest("form.foundation-form"),
	rteImage = $form.find("[name='./text']").val(),
	message, clazz = "coral-Button ";
	if(rteImage.includes('<img src="/content/dam/')){
	ns.ui.helpers.prompt({
	title: Granite.I18n.get("Invalid Input"),
	message: "Image not allowed on RTE component",
	actions: [{
	id: "CANCEL",
	text: "CANCEL",
	className: "coral-Button"
	}],
	callback: function (actionId) {
	if (actionId === "CANCEL") {
	}
	}
	});
	}else{
	$form.submit();
	}
	});
})(document, Granite.$, Granite.author);