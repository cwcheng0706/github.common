package com.zy.security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年10月22日 上午10:51:39
 */
public class HmacMD5 {

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
	private static byte[] initHmacMD5Key(String algorithm) throws Exception {
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
	private static byte[] encodeDataHmacMD5(String algorithm, byte[] key,byte[] msgData) throws Exception {
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

	public static void main(String[] args) throws Exception {
		byte[] key = initHmacMD5Key(ALGORITHM_HmacSHA512);
		byte[] msgData = "测试".getBytes(CHARSET_UTF8);
		byte[] data = encodeDataHmacMD5(ALGORITHM_HmacSHA512, key,msgData);

		//摘要
		String message = Hex.encodeHexString(data);
		System.out.println(new String(Hex.encodeHex(data)));
		System.out.println(message);

	}
}
