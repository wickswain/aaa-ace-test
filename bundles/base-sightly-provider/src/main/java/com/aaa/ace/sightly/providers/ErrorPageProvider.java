package com.aaa.ace.sightly.providers;

import com.adobe.cq.sightly.WCMUsePojo;

public class ErrorPageProvider extends WCMUsePojo {

	private String tempPath;
	public boolean errorPageFlag = false;

	@Override
	public void activate() throws Exception {
		tempPath = getCurrentPage().getProperties().get("cq:template", "");
		System.out.println("Template path  : " + tempPath);
		errorPageFlag = isErrorPage();
	}

	public boolean isErrorPage() {
		if (tempPath.equals("/apps/aaa-core/templates/errorpage")) {
			errorPageFlag = true;
		}
		return errorPageFlag;
	}

	public String getTempPath() {
		return tempPath;
	}
}
