package com.zy.security.jdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;

import org.bouncycastle.util.encoders.Hex;



public class MessageDigestCoderTest {
	
	public static void main(String[] args) throws Exception {
		
		byte[] data = "zy".getBytes("UTF-8");
		String msg = MessageDigestCoder.encryptBASE64(MessageDigestCoder.encryptHMac(data));
		System.out.println("zy: " + msg);
		
		
		
		testAlgorithmParameterGenerator();
		
		
		//测试mysql官网的文件MD5值
		System.out.println("=======测试文件MD5======");
		String md5 = "6d18197705a47f377636808d5d0a6348";
		InputStream in = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\mysql-server_5.6.21-1ubuntu14.04_i386.deb-bundle.tar"));
		byte[] b = MessageDigestCoder.encrypt(in, "md5");
		
		String en = Hex.toHexString(b);
		
		System.out.println(md5);
		System.out.println(en);
		
	}
	
	public static void testAlgorithmParameterGenerator() throws Exception {
		System.out.println("==========测试AlgorithmParameterGenerator========");
		AlgorithmParameterGenerator generator = AlgorithmParameterGenerator.getInstance("AES");
		System.out.println(generator.getProvider());
		
		generator.init(64);
		AlgorithmParameters ap = generator.generateParameters();
		byte[] bs = ap.getEncoded();
		String encodeString = new BigInteger(bs).toString();
		System.out.println(encodeString);
		
		
		AlgorithmParameters ap1 = AlgorithmParameters.getInstance("AES");
		ap1.init(new BigInteger(encodeString).toByteArray());
		byte[] b = ap1.getEncoded();
		System.out.println(new BigInteger(b).toString());
		
	}

}
