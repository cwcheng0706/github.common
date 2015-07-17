package com.zy.security;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public abstract class Coder {

	public static final String CHARSET_UTF_8 = "UTF-8";
	
	public static final String ALGORITHM_RSA = "RSA";
	public static final String ALGORITHM_DES = "DES";
	public static final String ALGORITHM_3DES = "DESSede";

	public static final String PROVIDER_BC = "BC";
	
	/**
	 * Java密钥库
	 */
	public static final String KEY_STORE_JKS = "JKS";
	public static final String KEY_STORE_P12 = "PKCS12";

	public static final String CERTIFICATE_TYPE_X509 = "X.509";
	public static final String SunX509 = "SunX509";
	public static final String SSL = "SSL";

	public static final String SSL_CERT_HEADER = "-----BEGIN CERTIFICATE-----";
	public static final String SSL_CERT_FOOTER = "-----END CERTIFICATE-----";
	
	public static final String SSL_REQUEST_HEADER = "-----BEGIN CERTIFICATE REQUEST-----";
	public static final String SSL_REQUEST_FOOTER = "-----END CERTIFICATE REQUEST-----";
	
	static {
		Security.addProvider(new BouncyCastleProvider());

		System.out.println("============静态加载BouncyCastleProvider start================");
		Provider[] ps = Security.getProviders();
		for (int i = 0; i < ps.length; i++) {
			System.out.println(ps[i].getName() + "  === " + ps[i].getInfo());
		}
		System.out.println("============静态加载BouncyCastleProvider end ================\n");
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * 十六进制（去掉0x开头）转二进制
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年3月20日 下午3:57:43
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
