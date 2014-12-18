package com.zy.execmd;

public class Test {

	public static void main(String[] args) {
		
		ExeCmd.process("keytool -genkey -v -alias server -keyalg RSA -storetype JKS -keystore c:\\client.p12  -dname CN=ss.jl.com,OU=IT,O=上海金鹿金融信息服务有限公司,L=shanghai,ST=shanghai,C=CN -validity 3650 -storepass IshmwQT7 -keypass IshmwQT7","GBK");
	}
}
