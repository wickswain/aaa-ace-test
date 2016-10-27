package com.aaa.ace.sightly.providers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

public class ServiceCardsColumnProvider extends WCMUsePojo {

	private static final Logger log = LoggerFactory.getLogger(ServiceCardsColumnProvider.class);
	private int requestedColumnCount;
	private List<String> serviceCardPaths;

	public void activate() throws Exception {
		log.debug("Start of ServiceCardsColumnProvider class");
		serviceCardPaths = new ArrayList<String>();

		try {

			String columnCount = getProperties().get("columnCount", String.class);
			if (StringUtils.isNotBlank(columnCount)) {
				requestedColumnCount = Integer.parseInt(getProperties().get("columnCount", String.class));
				log.info("requestedColumnCount" + requestedColumnCount);

				for (int i = 1; i <= requestedColumnCount; i++) {
					String cardPath = getResource().getName() + "_card_" + i;
					serviceCardPaths.add(cardPath);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("End of ServiceCardsColumnProvider class");
	}

	public List<String> getServiceCardPaths() {
		return serviceCardPaths;
	}
}
