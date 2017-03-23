package com.aaa.ace.sightly.providers;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to return an array of numbers by providing the
 * the number as input
 *
 */
public class NumberToSequenceArrayProvider extends WCMUsePojo {

	/**
	 * List variable.
	 */
	private ArrayList<Integer> list;

	@Override
	public void activate() throws Exception {

		String number = get("number", String.class);
		int length = 0;

		if (StringUtils.isNotBlank(number)) {
			length = Integer.parseInt(number);
			list = new ArrayList<Integer>();

			for (int i = 1; i <= length; i++) {
				list.add(i);
			}
		}
	}

	/**
	 * gets the array of numbers
	 *
	 * @return list
	 */
	public ArrayList<Integer> getList() {
		return list;
	}
}
