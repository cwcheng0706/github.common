package com.zy.security.jdk;

import org.bouncycastle.util.encoders.Hex;

public class AESCoderTest {

	public static void main(String[] args) throws Exception {
		testJdkAES();

		testBCAES();

	}

	public static void testJdkAES() throws Exception {
		System.out.println("====测试JDK自带的实现====");
		byte[] key = AESCoder.initKey();
		System.out.println("key：" + Hex.toHexString(key));
		String msg = "测试...";

		byte[] encryptData = AESCoder.encrypt(msg.getBytes("UTF-8"), key);
		System.out.println("加密：" + AESCoder.encryptBASE64(encryptData));

		byte[] decryptData = AESCoder.decrypt(encryptData, key);
		System.out.println("解密：" + new String(decryptData));

	}
	
	public static void testBCAES() throws Exception {
		System.out.println("====测试BC的实现====");
		byte[] key = AESCoder.initKeyBC();
		System.out.println("key：" + Hex.toHexString(key));
		String msg = "测试...";

		byte[] encryptData = AESCoder.encryptBC(msg.getBytes("UTF-8"), key);
		System.out.println("加密：" + AESCoder.encryptBASE64(encryptData));

		byte[] decryptData = AESCoder.decryptBC(encryptData, key);
		System.out.println("解密：" + new String(decryptData));

	}

}
