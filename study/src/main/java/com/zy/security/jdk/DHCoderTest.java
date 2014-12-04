package com.zy.security.jdk;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.bouncycastle.util.encoders.Hex;

import com.zy.security.Coder;

public abstract class DHCoderTest extends Coder {

	public Map<String, Object> map = new HashMap<String, Object>();

	public static void main(String[] args) throws Exception {
		KeyPair k1 = initSenderKeyPair();

		KeyPair k2 = initReceiverKeyPair(k1.getPublic());

		SecretKey s1 = DHCoder.initSecretKey(k2.getPublic().getEncoded(), k1.getPrivate().getEncoded());

		SecretKey s2 = DHCoder.initSecretKey(k1.getPublic().getEncoded(), k2.getPrivate().getEncoded());

		System.out.println(Hex.toHexString(s1.getEncoded()));
		System.out.println(Hex.toHexString(s2.getEncoded()));

		//s1密钥加密
		byte[] encryptData = DHCoder.encrypt("加密。。".getBytes("UTF-8"), s1.getEncoded());
		System.out.println("s1 加密后：" + Hex.toHexString(encryptData));
		
		//s2密钥解密
		byte[] decryptData = DHCoder.decrypt(encryptData, s2.getEncoded());
		System.out.println("s2 解密后：" + new String(decryptData,"UTF-8"));
	}

	private static KeyPair initSenderKeyPair() throws Exception {
		KeyPair keyPair = DHCoder.initKeyPair();
		return keyPair;
	}

	private static KeyPair initReceiverKeyPair(PublicKey pub) throws Exception {
		KeyPair keypair = DHCoder.initKeyPair(pub);
		return keypair;
	}
}
