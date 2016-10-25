package com.aaa.ace.sightly.providers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

public class ServiceCardsProvider extends WCMUsePojo {

	private static final Logger log = LoggerFactory.getLogger(ServiceCardsProvider.class);
	private int requestedRowCount;
	private List<String> serviceCardRowPaths;

	public void activate() throws Exception {
		log.debug("Start of ServiceCardsProvider class");
		serviceCardRowPaths = new ArrayList<String>();

		try {
			String rowCount = getProperties().get("rowCount", String.class);
			if (StringUtils.isNotBlank(rowCount)) {
				requestedRowCount = Integer.parseInt(getProperties().get("rowCount", String.class));

				for (int i = 1; i <= requestedRowCount; i++) {
					String rowPath = "servicecard_row_" + i;
					serviceCardRowPaths.add(rowPath);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("End of ServiceCardsProvider class");
	}

	public List<String> getServiceCardRowPaths() {
		return serviceCardRowPaths;
	}

}
