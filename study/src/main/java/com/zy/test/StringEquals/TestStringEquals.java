package com.zy.test.StringEquals;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;

public class TestStringEquals {

	public static void main(String[] args) throws Exception {

		// KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		//
		// generator.initialize(2048);
		// KeyPair keyPair = generator.generateKeyPair();
		// PrivateKey privateKeyPair = keyPair.getPrivate();
		// byte[] privateBytes = privateKeyPair.getEncoded();
		//
		// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		// KeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
		// Key privateKey = keyFactory.generatePrivate(privateKeySpec);

		String s1 = "accp";
		String s2 = new String("accp");
		String s3 = new String(s1);

		
		System.out.println("===equals==");
		System.out.println(s1.equals(s2));
		System.out.println(s1.equals(s3));
		System.out.println(s2.equals(s3));

		
		System.out.println("=== == ==");
		System.out.println(s1 == s2);
		System.out.println(s1 == s3);
		System.out.println(s2 == s3);
		
		System.out.println(s1 == "accp");
		System.out.println(s2 == "accp");
		System.out.println(s3 == "accp");
		
		System.out.println("===hashCode==");
		System.out.println(s1.hashCode() == s2.hashCode());
		System.out.println(s1.hashCode() == s3.hashCode());
		System.out.println(s2.hashCode() == s3.hashCode());
		

		// BigInteger src = new BigInteger("5");
		// System.out.println(src.toString(2));
		// System.out.println(5 & 8);
		//
		// System.out.println(3 << 3);

	}
}
