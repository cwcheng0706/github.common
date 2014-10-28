package com.zy.security.jdk;

import org.bouncycastle.util.encoders.Hex;

public class DESCoderTest {

	public static void main(String[] args) throws Exception {
		testJdkDES();
		
		testBCDES();
		
	}
	
	public static void testBCDES() throws Exception {
		System.out.println("====测试BC的实现====");
		byte[] key = DESCoder.initKeyBC();
		System.out.println("key：" + Hex.toHexString(key));
		String msg = "测试...";
		
		byte[] encryptData = DESCoder.encryptBC(msg.getBytes("UTF-8"), key);
		System.out.println("加密：" + DESCoder.encryptBASE64(encryptData));
		
		
		byte[] decryptData = DESCoder.decryptBC(encryptData, key);
		System.out.println("解密：" + new String(decryptData));
		
	}
	
	public static void testJdkDES() throws Exception {
		System.out.println("====测试JDK自带的实现====");
		byte[] key = DESCoder.initKey();
		System.out.println("key：" + Hex.toHexString(key));
		String msg = "测试...";
		
		byte[] encryptData = DESCoder.encrypt(msg.getBytes("UTF-8"), key);
		System.out.println("加密：" + DESCoder.encryptBASE64(encryptData));
		
		byte[] decryptData = DESCoder.decrypt(encryptData, key);
		System.out.println("解密：" + new String(decryptData));
		
	}
}
