package com.zy.security.jdk;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.zy.security.Coder;

/**
 * 证书组件
 * 
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年10月21日 下午2:18:36
 */
public abstract class CertificateCoder extends Coder {
	
	private static Log logger = LogFactory.getLog(CertificateCoder.class);

	/**
	 * Java密钥库
	 */
	public static final String KEY_STORE_JKS = "JKS";
	public static final String KEY_STORE_P12 = "PKCS12";

	public static final String X509 = "X.509";
	public static final String SunX509 = "SunX509";
	public static final String SSL = "SSL";

	public static final String SSL_CERT_HEADER = "-----BEGIN CERTIFICATE-----";
	public static final String SSL_CERT_FOOTER = "-----END CERTIFICATE-----";
	

	public static X509Certificate getX509Certificate(String cert) {
		X509Certificate x509Certificate = null;
		if (null != cert && !"".equals(cert.trim())) {
			cert = cert.trim();
			cert = cert.replaceAll(SSL_CERT_HEADER, "").replaceAll(SSL_CERT_FOOTER, "");
			cert = replaceBlank(cert);
			try{
				// Base64解码
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] byteCert = decoder.decodeBuffer(cert);
				// 转换成二进制流
				ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
				CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
				x509Certificate = (X509Certificate) certificateFactory.generateCertificate(bain);
				String info = x509Certificate.getSubjectDN().getName();
				logger.debug("证书拥有者:" + info);
			}catch(Exception e) {
				logger.error("读取证书异常." + e);
			}
		}

		return x509Certificate;
	}
	
	public static X509CRL loadX509CRL(String crlFilePath) {
		X509CRL crl = null;
		FileInputStream in = null;
		try{
			in = new FileInputStream(crlFilePath);
			CertificateFactory cf = CertificateFactory.getInstance(X509);
			crl = (X509CRL) cf.generateCRL(in);
			
		}catch(Exception e) {
			logger.error("加载吊销列表异常." + e);
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				in = null;
				logger.error(e);
			}
		}
		return crl;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 由KeyStore获得私钥
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKey(String keyStorePath, String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());
		return key;
	}

	/**
	 * 由Certificate获得公钥
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	private static PublicKey getPublicKey(String certificatePath) throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		PublicKey key = certificate.getPublicKey();
		return key;
	}

	/**
	 * 获得Certificate
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String certificatePath) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
		FileInputStream in = new FileInputStream(certificatePath);

		Certificate certificate = certificateFactory.generateCertificate(in);
		in.close();

		return certificate;
	}

	/**
	 * 获得Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String keyStorePath, String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		Certificate certificate = ks.getCertificate(alias);

		return certificate;
	}

	/**
	 * 获得KeyStore
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(KEY_STORE_JKS);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String certificatePath) throws Exception {

		// 取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String certificatePath) throws Exception {
		// 取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	/**
	 * 验证Certificate
	 * 
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(String certificatePath) {
		return verifyCertificate(new Date(), certificatePath);
	}

	/**
	 * 验证Certificate是否过期或无效
	 * 
	 * @param date
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(Date date, String certificatePath) {
		boolean status = true;
		try {
			// 取得证书
			Certificate certificate = getCertificate(certificatePath);
			// 验证证书是否过期或无效
			status = verifyCertificate(date, certificate);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 验证证书是否过期或无效
	 * 
	 * @param date
	 * @param certificate
	 * @return
	 */
	private static boolean verifyCertificate(Date date, Certificate certificate) {
		boolean status = true;
		try {
			X509Certificate x509Certificate = (X509Certificate) certificate;
			x509Certificate.checkValidity(date);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 签名
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] sign, String keyStorePath, String alias, String password) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias, password);
		// 获取私钥
		KeyStore ks = getKeyStore(keyStorePath, password);
		// 取得私钥
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());

		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initSign(privateKey);
		signature.update(sign);
		return encryptBASE64(signature.sign());
	}

	/**
	 * 验证签名
	 * 
	 * @param data
	 * @param sign
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String sign, String certificatePath) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
		// 获得公钥
		PublicKey publicKey = x509Certificate.getPublicKey();
		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initVerify(publicKey);
		signature.update(data);

		return signature.verify(decryptBASE64(sign));

	}

	/**
	 * 验证Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(Date date, String keyStorePath, String alias, String password) {
		boolean status = true;
		try {
			Certificate certificate = getCertificate(keyStorePath, alias, password);
			status = verifyCertificate(date, certificate);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 验证Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(String keyStorePath, String alias, String password) {
		return verifyCertificate(new Date(), keyStorePath, alias, password);
	}

	/**
	 * 获得SSLSocektFactory
	 * 
	 * @param password
	 *            密码
	 * @param keyStorePath
	 *            密钥库路径
	 * 
	 * @param trustKeyStorePath
	 *            信任库路径
	 * @return
	 * @throws Exception
	 */
	private static SSLSocketFactory getSSLSocketFactory(String password, String keyStorePath, String trustKeyStorePath) throws Exception {
		// 初始化密钥库
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SunX509);
		KeyStore keyStore = getKeyStore(keyStorePath, password);
		keyManagerFactory.init(keyStore, password.toCharArray());

		// 初始化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(SunX509);
		KeyStore trustkeyStore = getKeyStore(trustKeyStorePath, password);
		trustManagerFactory.init(trustkeyStore);

		// 初始化SSL上下文
		SSLContext ctx = SSLContext.getInstance(SSL);
		ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		SSLSocketFactory sf = ctx.getSocketFactory();

		return sf;
	}

	/**
	 * 为HttpsURLConnection配置SSLSocketFactory
	 * 
	 * @param conn
	 *            HttpsURLConnection
	 * @param password
	 *            密码
	 * @param keyStorePath
	 *            密钥库路径
	 * 
	 * @param trustKeyStorePath
	 *            信任库路径
	 * @throws Exception
	 */
	public static void configSSLSocketFactory(HttpsURLConnection conn, String password, String keyStorePath, String trustKeyStorePath) throws Exception {
		conn.setSSLSocketFactory(getSSLSocketFactory(password, keyStorePath, trustKeyStorePath));
	}
}
