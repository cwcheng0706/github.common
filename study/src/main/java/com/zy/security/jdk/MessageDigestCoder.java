package com.zy.security.jdk;

import java.security.DigestInputStream;
import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.zy.security.Coder;

public abstract class MessageDigestCoder extends Coder {

	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";

	/**
	 * MAC算法可选以下多种算法
	 * 
	 * <pre>
	 * HmacMD5 
	 * HmacSHA1 
	 * HmacSHA256 
	 * HmacSHA384 
	 * HmacSHA512
	 * </pre>
	 */
	public static final String KEY_MAC = "HmacMD5";

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);

		return md5.digest();

	}
	
	/**
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月27日 下午3:55:41
	 * @param dis
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(DigestInputStream dis) throws Exception {

		return null;
	}

	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {

		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);

		return sha.digest();

	}

	/**
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年10月27日 上午10:49:57
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMac(byte[] data) throws Exception {
		String key = initMacKey();
		return encryptHMAC(data, key);
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	private static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);

		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptHMAC(byte[] data, String key) throws Exception {

		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);

		return mac.doFinal(data);

	}
}
