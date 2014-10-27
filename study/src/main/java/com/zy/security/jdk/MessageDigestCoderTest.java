package com.zy.security.jdk;

import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;


public class MessageDigestCoderTest {
	
	public static void main(String[] args) throws Exception {
		
		byte[] data = "zy".getBytes("UTF-8");
		String msg = MessageDigestCoder.encryptBASE64(MessageDigestCoder.encryptHMac(data));
		System.out.println(msg);
		
		
		
		testAlgorithmParameterGenerator();
		
		
	}
	
	public static void testAlgorithmParameterGenerator() throws Exception {
		
		AlgorithmParameterGenerator generator = AlgorithmParameterGenerator.getInstance("DES");
		generator.init(56);
		AlgorithmParameters ap = generator.generateParameters();
		byte[] bs = ap.getEncoded();
		String encodeString = new BigInteger(bs).toString();
		System.out.println(encodeString);
		
		
		AlgorithmParameters ap1 = AlgorithmParameters.getInstance("DES");
		ap1.init(new BigInteger(encodeString).toByteArray());
		byte[] b = ap1.getEncoded();
		System.out.println(new BigInteger(b).toString());
		
	}

}
