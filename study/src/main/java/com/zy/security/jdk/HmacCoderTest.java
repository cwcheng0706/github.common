package com.zy.security.jdk;

import org.apache.commons.codec.binary.Hex;

public class HmacCoderTest {


	public static void main(String[] args) throws Exception {
		byte[] key = HmacCoder.initHmacKey(HmacCoder.ALGORITHM_HmacSHA512);
		byte[] msgData = "测试".getBytes(HmacCoder.CHARSET_UTF8);
		byte[] data = HmacCoder.encrypt(HmacCoder.ALGORITHM_HmacSHA512, key,msgData);

		//摘要
		String message = Hex.encodeHexString(data);
		System.out.println(new String(Hex.encodeHex(data)));
		System.out.println(message);

	}
}
