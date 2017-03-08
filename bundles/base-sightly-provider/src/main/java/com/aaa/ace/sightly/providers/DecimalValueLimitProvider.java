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

    private static final Logger log = LoggerFactory.getLogger(DecimalValueLimitProvider.class);

    private String decimalLimtvalue;

    @Override
    public void activate() throws Exception {
        String value = get("value", String.class);
        NumberFormat nf = NumberFormat.getInstance();
        Double tempValue = Double.parseDouble(value);
        nf.setMaximumFractionDigits(2);
        decimalLimtvalue = nf.format(tempValue);
    }
    
    public String getDecimalLimtvalue() {
        return decimalLimtvalue;
    }
}