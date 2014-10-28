package com.zy.security.jdk;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.zy.security.Coder;

/**
 * 密钥长度想更长的话  需要下载http://www.oracle.com/technetwork/java/javase/downloads/index.html 里面的jce_policy
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年10月28日 下午2:50:43
 */
public class AESCoder extends Coder {

	/**
	 * java jdk只支持128(默认),192,256位长度，bouncy Castle 支持的长度与JDK一致
	 * 
	 */
	public static final String ALGORITHM = "AES";

	/**
	 * 加密解密算法/工作模式/填充方式  这里只罗列两种
	 */
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_BC = "AES/ECB/PKCS7Padding";
	
	/**
	 * java jdk只支持128(默认，由于美国 基于安全问题其它长度不能使用),192,256位长度，bouncy Castle 支持的长度与JDK一致
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月28日 下午2:23:43
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		System.out.println("Provider:" + keyGenerator.getProvider());
		keyGenerator.init(256);
		
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * bouncycastle (jdk只支持128(默认),192,256位长度，bouncy Castle 支持的长度与JDK一致)
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月28日 下午1:27:12
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKeyBC() throws Exception {
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM,"BC");
		System.out.println("Provider:" + keyGenerator.getProvider());
		keyGenerator.init(256);
		
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * 加密
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月28日 下午1:29:53
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptBC(byte[] data, byte[] key) throws Exception {
		// 还原秘密密钥
		Key k = toKey(key);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_BC,"BC");
		// 初始化，设置解密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月28日 下午1:30:03
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBC(byte[] data, byte[] key) throws Exception {
		// 还原秘密密钥
		Key k = toKey(key);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_BC,"BC");
		// 初始化，设置解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	
	/**
	 * 加密
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月28日 下午1:29:53
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 还原秘密密钥
		Key k = toKey(key);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置解密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月28日 下午1:30:03
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 还原秘密密钥
		Key k = toKey(key);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	private static Key toKey(byte[] key) throws Exception {

		// 实例化DES密钥材料
		SecretKey secretKey = new SecretKeySpec(key,ALGORITHM);

		return secretKey;
	}
}
