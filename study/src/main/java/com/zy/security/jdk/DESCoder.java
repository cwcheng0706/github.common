package com.zy.security.jdk;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.zy.security.Coder;

public class DESCoder extends Coder {

	/**
	 * java jdk只支持56位长度，bouncy Castle 支持64位长度
	 */
	public static final String ALGORITHM = "DES";

	/**
	 * 加密解密算法/工作模式/填充方式
	 */
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_BC = "DES/ECB/PKCS7Padding";
	
	/**
	 * JDK提供56位长度
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月28日 下午1:27:12
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		System.out.println("Provider:" + keyGenerator.getProvider());
		keyGenerator.init(56);
		
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}
	
	/**
	 * bouncycastle提供更长的64位长度
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
		keyGenerator.init(64);
		
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
		DESKeySpec desKeySpec = new DESKeySpec(key);

		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);

		// 生成秘密密钥
		SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

		return secretKey;
	}

}
