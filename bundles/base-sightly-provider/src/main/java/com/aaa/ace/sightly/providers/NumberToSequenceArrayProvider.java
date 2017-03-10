package com.aaa.ace.sightly.providers;

import java.util.ArrayList;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * This sightly provider is used to get the child resource properties of current
 * resource.
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
		int length = Integer.parseInt(number);
		list = new ArrayList<Integer>();

		for (int i = 1; i <= length; i++) {
			list.add(i);
		}
	}

	/**
	 * gets the list containing numbers
	 *
	 * @return list
	 */
	public ArrayList<Integer> getList() {
		return list;
	}
}
