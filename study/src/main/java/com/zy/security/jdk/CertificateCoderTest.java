package com.zy.security.jdk;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.HttpsURLConnection;

import org.bouncycastle.util.io.pem.PemReader;
import org.junit.Test;

/**
 * 
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年10月21日 下午2:19:18
 */
public class CertificateCoderTest {
	private String password = "123456";
	private String alias = "www.zlex.org";
	private String certificatePath = "d:/zlex.cer";
	private String keyStorePath = "d:/zlex.keystore";
	private String clientKeyStorePath = "d:/zlex-client.keystore";
	private String clientPassword = "654321";

	@Test
	public void testGetX509Certificate() throws Exception {

		// 加密
		byte[] b1 = CertificateCoder.encryptByPublicKey("测试".getBytes(), "D:\\ssl\\79.110\\zhuyong001.crt");

		String str = "MIIEpQIBAAKCAQEA16RTTdXRrLAtdmWHkEGF64OyHuaVS/6rmjyRWqjs3c6Gbz6J"+
		"uAWt9MBFAFrJYJubPzZ8SZ6W63OXWfwtv0yr689yRu2SUjLj9sYO+krt2J9aSDqB"+
		"vYt9bU4/j29QQ10Bx9KLPwvyvtqLUoJsEwBaYbcoMlxr5g8pVwUM8N3WXc8Qja6e"+
		"LOeDENtUADeUtYptyZ9njne3rPSnrAAiQjgiKUKkcdpIYjUlXaVdB1VneYiGiSbq"+
		"E1DwZRfNGvqCGgG9vgSZmhb6uidrUYH9TV2Em0qSIkIEgfB5R/OVz7RkjD6k6EDM"+
		"6LyEqdrXDWLmREB9IBfOM7Jxk+d1aSMrl1D+awIDAQABAoIBAQCepi8DnMPeIN/R"+
		"s3DBGPPGCeYKBerhNkRZ/SWTZaSeq0sMnR4FXWhWIP6i/CgcA198akGWiC7QmwJH"+
		"GdkuXOI/YyfYNwDnqLk+3Qg37Nh//v0VFrZNFUr6GA97H5IVfV/J+4Smpcu5zl+i"+
		"HIIWmLPmIDJamtRnNdBcTULwEsWbzVnD4Jik9ch+hJoCR0krGnuefi+mfMlhhM0m"+
		"NUvZb62Wg9XCe7lwlyuMXurRYUP7KvMnaF5witcK6+pxB4fjmS+BAZ9IibLgL9kI"+
		"AbLg74m5N3bizSqGDauCkSzI84M6O13vwSIChknndgARWWb7CJwm6G4DWgTuRJ6J"+
		"u36BQneBAoGBAO7HB/gucx2/9VXO1n4hA6YXv1NSYIFTtQWM17dm2u/waCFaJ+7B"+
		"O7/G0gsRsrmuL0U9DP3s8PdZ/p3x0aJhleZh9D4rAtore2l5KrL3GU+yGYcI/Vet"+
		"J7GWCRISy8wW/rXmYjXonrQhHfQL9V0ShxDG4K1VOePbVoN9j1zVGcapAoGBAOcy"+
		"GfzpcAjIDsRDIIyxIrbpbHhzwKXbGxyFK831gp9PD4qTwoMwfur8afFTfBUn/lLp"+
		"fzIPTcumHkuX1yVn94bbW2jt/YMXLH08vRbB+LarbQSsQSnybmJDHXI6CjbGuarn"+
		"B6eBBzGwBziuoqDqHNzeZO7wsVeFSj1+MS2frYzzAoGAOF31zKmydTBuiw4j5myG"+
		"vvvWJ6ZHr+AB2MiR1/LI9mFrQ4xT5vHo1/HofcJ0zY6IDDFIUt1PWST+jWybj1i4"+
		"GUXAcbiXmMcxAAohgVQBvqWNjyoZHEx9FVgU1hZn08sN4AT95DoA1QcIWidEie8o"+
		"wVYhjemTm+2Yc7v9Tu3jgtkCgYEAinkqsZ3L2aahmZGvLDVoWm/i5R5Hc8+RJHrq"+
		"6rgy3WJIJ7MqnNLuIaa/eaiMOEu0+0oZQg2ChghgOtyeIyxrSF2YGXf6EItPvU8k"+
		"3j2dzyNL77GQciRfUwFp4KG8vmQ2jpNDtWMFwQ/ys1N92q1+ThfyEl8diRMvjTdJ"+
		"E0+BblsCgYEAqM3ODZRE0QJ8HaHip98gyS9EeYYFGckjRI0J1UaKb2q5e7Lm36so"+
		"7At9pFJNpwZbYjJbSK1b318vkig+REbcF07pMVQ6LAs+61xy9SEORneSibfTwHyw"+
		"3xcAQ+jMeHxGacPWgmwkVBbVMLngA14Bz8WO7XwOfm+ZEmkRK7nlJlY=";
		 
		 KeyFactory kf = KeyFactory.getInstance("RSA");
		 PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(CertificateCoder.decryptBASE64(str));
		 PrivateKey privateKey = kf.generatePrivate(keySpec);
		 System.out.println("privateKey【" + privateKey + "】");

		// 解密1
		byte[] b2 = CertificateCoder.decryptByPrivateKey(b1,privateKey);
		
		System.out.println("解密后【" + new String(b2) +"】");

		//解密2
		byte[] b3 =CertificateCoder.decryptByPrivateKey(b1,CertificateCoder.getPrivateKey(new File("D:\\ssl\\79.110\\pkcs8_der.key")));
		System.out.println("解密后【" + new String(b3) +"】");
		
		
		
	}

	@Test
	public void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String inputStr = "Ceritifcate";
		byte[] data = inputStr.getBytes();

		byte[] encrypt = CertificateCoder.encryptByPublicKey(data, certificatePath);

		byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt, keyStorePath, alias, password);
		String outputStr = new String(decrypt);

		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

		// 验证数据一致
		// assertArrayEquals(data, decrypt);

		// 验证证书有效
		// assertTrue(CertificateCoder.verifyCertificate(certificatePath));

	}

	@Test
	public void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");

		String inputStr = "sign";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = CertificateCoder.encryptByPrivateKey(data, keyStorePath, alias, password);

		byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData, certificatePath);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		// assertEquals(inputStr, outputStr);

		System.err.println("私钥签名——公钥验证签名");
		// 产生签名
		String sign = CertificateCoder.sign(encodedData, keyStorePath, alias, password);
		System.err.println("签名:\r" + sign);

		// 验证签名
		boolean status = CertificateCoder.verify(encodedData, sign, certificatePath);
		System.err.println("状态:\r" + status);
		// assertTrue(status);

	}

	@Test
	public void testHttps() throws Exception {
		URL url = new URL("https://www.zlex.org/examples/");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		conn.setDoInput(true);
		conn.setDoOutput(true);

		CertificateCoder.configSSLSocketFactory(conn, clientPassword, clientKeyStorePath, clientKeyStorePath);

		InputStream is = conn.getInputStream();

		int length = conn.getContentLength();

		DataInputStream dis = new DataInputStream(is);
		byte[] data = new byte[length];
		dis.readFully(data);

		dis.close();
		System.err.println(new String(data));
		conn.disconnect();
	}
}
