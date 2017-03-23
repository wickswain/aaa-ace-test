package com.aaa.ace.sightly.providers;

import java.text.NumberFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to limit the decimal values up to two digits.
 *
 */

public class DecimalValueLimitProvider extends WCMUsePojo {

	private static final Logger logger = LoggerFactory
			.getLogger(DecimalValueLimitProvider.class);

	private String decimalLimtvalue;

	@Override
	public void activate() throws Exception {
		logger.info("Start of DecimalValueLimitProvider Sightly provider class activate method");
		logger.debug("attribute value" + get("value", String.class));
		String value = get("value", String.class);
		if (value != null) {
			NumberFormat nf = NumberFormat.getInstance();
			Double tempValue = Double.parseDouble(value);
			nf.setMaximumFractionDigits(2);
			decimalLimtvalue = nf.format(tempValue);
			logger.info("End of DecimalValueLimitProvider Sightly provider class activate method");
		} else {
			logger.debug("Null attribute value passed to DecimalValueLimitProvider Sightly provider class");
		}
	}

	public String getDecimalLimtvalue() {
		return decimalLimtvalue;
	}
}