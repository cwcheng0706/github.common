package com.zy.security.jdk;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

import com.zy.security.Coder;

public class SingatureCoder extends Coder {

	public static void main(String[] args) throws Exception {

		testSign();
		
	}

	public static void testSign() throws Exception {

		byte[] data = "测试签名。".getBytes("UTF-8");

		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);

		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		System.out.println(keyPairGenerator.getAlgorithm());

		Signature signature = Signature.getInstance(keyPairGenerator.getAlgorithm());
		signature.initSign(keyPair.getPrivate());
		signature.update(data);
		byte[] sign = signature.sign();

		System.out.println("私钥签名完成【" + MessageDigestCoder.encryptBASE64(sign) + "】");

		// 公钥验签
		signature.initVerify(keyPair.getPublic());
		signature.update(data);
		boolean verify = signature.verify(sign);
		System.out.println("公钥验签结果【" + verify + "】");

	}
}
