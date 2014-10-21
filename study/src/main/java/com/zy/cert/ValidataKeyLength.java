package com.zy.cert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 验证生成密钥的长度
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年10月21日 下午4:44:31
 */
public class ValidataKeyLength {

	public static void main(String[] args) throws Exception {
		
		KeyGenerator generator = KeyGenerator.getInstance("DES");
		generator.init(256);
		SecretKey secretKey = generator.generateKey();
		
		byte[] key = secretKey.getEncoded();
		
		/**
		 * 由于美国出口的限制，只支持56位长度的密钥，这样安全性就比较低。如果你所在的国家和地区允许的话，可以获得相应的权限，就得去官方去下载JCE包
		 * Exception in thread "main" java.security.InvalidParameterException: Wrong keysize: must be equal to 56
				at com.sun.crypto.provider.DESKeyGenerator.engineInit(DESKeyGenerator.java:90)
				at javax.crypto.KeyGenerator.init(KeyGenerator.java:501)
				at javax.crypto.KeyGenerator.init(KeyGenerator.java:478)
				at com.zy.cert.ValidataKeyLength.main(ValidataKeyLength.java:18)

		 */
	}
}
