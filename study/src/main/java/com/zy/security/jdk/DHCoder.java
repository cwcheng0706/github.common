package com.zy.security.jdk;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.zy.security.Coder;

public abstract class DHCoder extends Coder {
	
	public static final String ALGORITHM_SECRETKEY = "AES";

	/**
	 * 初始化密钥对
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年11月26日 下午4:42:51
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public KeyPair initKeyPair() throws Exception {
		KeyPair keyPair = null;
		
		//实例化密钥对生成器
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
		//初始化密钥对生成器
		keyPairGenerator.initialize(1034);
		//生成密钥对
		keyPair = keyPairGenerator.generateKeyPair();
		
		return keyPair;
	}
	
	/**
	 * 根据对方公钥构建密钥对,即以乙方（消息接受方）
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年11月26日 下午4:51:09
	 * @param publicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException
	 */
	public KeyPair initKeyPair(PublicKey publicKey) throws Exception {
		KeyPair keyPair = null;
		
		KeyFactory keyFactory = KeyFactory.getInstance("DH");
		//由甲方公钥生成乙方密钥
		DHParameterSpec dhParameterSpec = ((DHPublicKey)publicKey).getParams();
		//初始化密钥对生成器
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyFactory.getAlgorithm());
//		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
		keyPairGenerator.initialize(dhParameterSpec);
		
		//生成密钥对
		keyPair = keyPairGenerator.generateKeyPair();
		
		return keyPair;
	}
	
	/**
	 * 生成秘密密钥
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年11月26日 下午5:28:49
	 * @param _publicKey 乙方公钥
	 * @param _privateKey 己方私钥
	 * @return
	 * @throws Exception
	 */
	public SecretKey initSecretKey(byte[] _publicKey,byte[] _privateKey) throws Exception {
		SecretKey secretKey = null;
		
		//实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance("DH");
		
		//公钥材料转换
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(_publicKey);
		//生成公钥
		PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		
		//私钥材料转换
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(_privateKey);
		//生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		
		KeyAgreement keyAgreement = KeyAgreement.getInstance(keyFactory.getAlgorithm());
		
		//生成秘密密钥
		keyAgreement.init(privateKey);
		keyAgreement.doPhase(publicKey, true);
		
		secretKey = keyAgreement.generateSecret(ALGORITHM_SECRETKEY);
		
		return secretKey;
	}
	
	/**
	 * 加密
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年11月26日 下午5:33:57
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_SECRETKEY);
		
		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		
		return cipher.doFinal(data);
	}
}
