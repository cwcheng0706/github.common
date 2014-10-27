package com.zy.security;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public abstract class Coder {
	
	public static final String ALGORITHM_DES = "DES";
	public static final String ALGORITHM_3DES = "DESSede";

	static {
		Security.addProvider(new BouncyCastleProvider());
		
		System.out.println("============静态加载BouncyCastleProvider start================");
		Provider[] ps = Security.getProviders();
		for(int i = 0 ; i < ps.length ; i++ ) {
			System.out.println(ps[i].getName() + "  === " + ps[i].getInfo());
		}
		System.out.println("============静态加载BouncyCastleProvider end ================\n");
	}
	
	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}
}
