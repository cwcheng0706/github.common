package com.zy.security.jdk;

import org.bouncycastle.util.encoders.Hex;

public class DESedeCoderTest {

	public static void main(String[] args) throws Exception {
		testJdkDESede();
		
		testBCDESede();
		
	}
	
	public static void testBCDESede() throws Exception {
		System.out.println("====测试BC的实现====");
		byte[] key = DESedeCoder.initKeyBC();
		System.out.println("key：" + Hex.toHexString(key));
		String msg = "测试...";
		
		byte[] encryptData = DESedeCoder.encryptBC(msg.getBytes("UTF-8"), key);
		System.out.println("加密：" + DESedeCoder.encryptBASE64(encryptData));
		
		
		byte[] decryptData = DESedeCoder.decryptBC(encryptData, key);
		System.out.println("解密：" + new String(decryptData));
		
	}
	
	public static void testJdkDESede() throws Exception {
		System.out.println("====测试JDK自带的实现====");
		byte[] key = DESedeCoder.initKey();
		System.out.println("key：" + Hex.toHexString(key));
		String msg = "测试...";
		
		byte[] encryptData = DESedeCoder.encrypt(msg.getBytes("UTF-8"), key);
		System.out.println("加密：" + DESedeCoder.encryptBASE64(encryptData));
		
		byte[] decryptData = DESedeCoder.decrypt(encryptData, key);
		System.out.println("解密：" + new String(decryptData));
		
	}
}
