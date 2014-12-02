package com.zy.security.jdk;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class HmacCoder {


	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String ALGORITHM_HMACMD5 = "HmacMD5";
	public static final String ALGORITHM_HmacSHA512 = "HmacSHA512";

	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月22日 下午4:50:06
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static byte[] initHmacKey(String algorithm) throws Exception {
		// 初始化KeyGenerator
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);

		// 产生密钥
		SecretKey secretKey = keyGenerator.generateKey();

		// 获得密钥
		byte[] key = secretKey.getEncoded();
		return key;
	}

	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月22日 下午4:50:01
	 * @param algorithm
	 * @param msgData
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String algorithm, byte[] key,byte[] msgData) throws Exception {
		// 密钥还原
		SecretKey secretKey = new SecretKeySpec(key, algorithm);

		// 实例化Mac
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());

		// 初始化Mac
		mac.init(secretKey);

		// 执行消息摘要
		byte[] data = mac.doFinal(msgData);
		return data;

	}

}
