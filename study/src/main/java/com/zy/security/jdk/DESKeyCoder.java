package com.zy.security.jdk;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.zy.security.Coder;

public class DESKeyCoder extends Coder {

	
	private static KeyGenerator keyGenerator;

	static {
		try {
			keyGenerator = KeyGenerator.getInstance(ALGORITHM_DES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		//1.
		testSecreKeyWrap();
		
		//2.
		testDESKeySpec();
		
	}
	
	/**
	 * 测试秘密密钥SecretKey 还原 与 testSecreKeyWrap方法里面的还原其实一致，只不过SecretKeySpec兼容所有对称加密算法面DESKeySpec是指定DES算法
	 * 如果是三重DES 把ALGORITHM_DES-->ALGORITHM_3DES ,DESKeySpec--->DESedeKeySpec 即可
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月27日 下午4:46:46
	 * @throws Exception
	 */
	public static void testDESKeySpec() throws Exception {
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] secretKeyByte = secretKey.getEncoded();
		System.out.println("原始秘密密钥：" + MessageDigestCoder.encryptBASE64(secretKeyByte));
		
		DESKeySpec desKeySpec = new DESKeySpec(secretKeyByte);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
		SecretKey secretKey1 = secretKeyFactory.generateSecret(desKeySpec);
		System.out.println("还原秘密密钥：" + MessageDigestCoder.encryptBASE64(secretKey1.getEncoded()));
		
	}

	/**
	 * 
	 * 测试秘密密钥SecretKey 的包装及解包
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月27日 下午4:22:49
	 * @param args
	 * @throws Exception
	 */
	public static void testSecreKeyWrap() throws Exception {

		
		//1.测试秘密密钥包装及解包
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] secretKeyByte = secretKey.getEncoded();
		System.out.println("SecretKey包装前：" + MessageDigestCoder.encryptBASE64(secretKeyByte));
		
		byte[] wrapKey = testWrap(secretKeyByte);
		System.out.println("SecretKey包装后：" + MessageDigestCoder.encryptBASE64(wrapKey));
		
		byte[] unwrapKey = testUnwarp(wrapKey,secretKeyByte);
		System.out.println("SecretKey解包后：" + MessageDigestCoder.encryptBASE64(unwrapKey));
	}

	private static byte[] testWrap(byte[] secretKeyByte) throws Exception {
		
		SecretKey secretKey = new SecretKeySpec(secretKeyByte,ALGORITHM_DES);

		// 实例 化Cipher 并包装秘密密钥Key
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		cipher.init(Cipher.WRAP_MODE, secretKey);
		byte[] key = cipher.wrap(secretKey);
		
		return key;
	}

	private static byte[] testUnwarp(byte[] k,byte[] secretKeyByte) throws Exception {
		
		SecretKey secretKey = new SecretKeySpec(secretKeyByte,ALGORITHM_DES);
		// 实例 化Cipher 并包装秘密密钥Key
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		cipher.init(Cipher.UNWRAP_MODE, secretKey);
		Key key = cipher.unwrap(k, ALGORITHM_DES, Cipher.SECRET_KEY);
		
		return key.getEncoded();
	}
}
