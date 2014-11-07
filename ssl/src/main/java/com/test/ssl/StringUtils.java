package com.test.ssl;

public class StringUtils {

	public static boolean isEmpty(String property) {
		if(null == property || "".equals(property.trim())) {
			return true;
		}
		return false;
	}

}
