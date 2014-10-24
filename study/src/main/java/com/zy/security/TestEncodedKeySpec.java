package com.zy.security;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public class TestEncodedKeySpec {
	
	public static void main(String[] args) throws Exception{
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

		generator.initialize(2048);
		KeyPair keyPair = generator.generateKeyPair();
		PrivateKey privateKeyPair = keyPair.getPrivate();
		byte[] privateBytes = privateKeyPair.getEncoded();
		
		
		
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
		Key privateKey = keyFactory.generatePrivate(privateKeySpec);
		
		
		//目前没用11
		FileOutputStream fos = new FileOutputStream(new File("c:\\test.p12"));
		fos.write(privateBytes);
		fos.flush();
	}

}
