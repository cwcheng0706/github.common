package com.zy.http;

public class C {

	private C() {
	}

	public static final String SUCCUSS_CODE = "000000";

	public static class Certificate {
		public static final String KEY_STORE_TYPE_JKS = "jks";
		public static final String KEY_STORE_TYPE_P12 = "PKCS12";
		public static final String PROTOCOL_SSL = "SSL";
		public static final String ALGORITHM_SUNX509 = "SunX509";
	}

	public static class Http {

		public static final int CODE_200 = 200;
		public static final int CODE_400 = 400;

		public static final String METHOD_GET = "GET";
		public static final String METHOD_POST = "POST";

		public static final String CONTENT_TYPE = "Content-Type";
		public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

	}

	public static class Symbol {
		public static final String QUESTION_MARK = "?";
		public static final String AND_MARK = "&";
		public static final String EQUAL_MARK = "=";
	}

	public static class Clazz {
		public static final String CLAZZ = "class";
	}

	public static class Charset {
		public static final String UTF8 = "UTF-8";
	}

	public static class DateFormat {
		/** yyyy-MM-dd HH:mm:ss **/
		public static final String f1 = "yyyy-MM-dd HH:mm:ss";
		public static final String f2 = "yyyyMMddHHmmss";
	}
}
