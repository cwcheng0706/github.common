package com.zy.security.jdk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerValue;

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

	

	public static void main(String[] args) throws Exception {
		

	}
	

	/**
	 * 输入参数为base64编码的证书串
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年11月18日 下午2:40:35
	 * @param cert
	 * @return
	 */
	public static X509Certificate getX509CertificateFromPem(String cert) {
		X509Certificate x509Certificate = null;
		if (null != cert && !"".equals(cert.trim())) {
			cert = cert.trim();
			cert = cert.replaceAll(SSL_CERT_HEADER, "").replaceAll(SSL_CERT_FOOTER, "");
			cert = replaceBlank(cert);
			try {
				// Base64解码
//				BASE64Decoder decoder = new BASE64Decoder();
//				byte[] byteCert = decoder.decodeBuffer(cert);
				
				
				byte[] byteCert = org.bouncycastle.util.encoders.Base64.decode(cert.getBytes(CHARSET_UTF_8));
				
				// 转换成二进制流
				ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
				CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509);
				x509Certificate = (X509Certificate) certificateFactory.generateCertificate(bain);
				String info = x509Certificate.getSubjectDN().getName();
				logger.debug("证书拥有者:" + info);
			} catch (Exception e) {
				logger.error("读取证书异常." + e);
			}
		}

		return x509Certificate;
	}

	public static X509Certificate getX509Certificate(byte[] bytes) {
		X509Certificate x509Certificate = null;
		try {
			// 转换成二进制流
			ByteArrayInputStream bain = new ByteArrayInputStream(bytes);
			CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509);
			x509Certificate = (X509Certificate) certificateFactory.generateCertificate(bain);
			String info = x509Certificate.getSubjectDN().getName();
			logger.debug("证书拥有者:" + info);
		} catch (Exception e) {
			logger.error("读取证书异常." + e);
		}

		return x509Certificate;
	}

	/**
	 * 输入参数：证书文件
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年11月18日 下午2:45:35
	 * @param file
	 * @return
	 */
	public static X509Certificate getX509Certificate(File file) {
		X509Certificate x509Certificate = null;
		InputStream inStream = null;
		try {
			inStream = new FileInputStream(file);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			x509Certificate = (X509Certificate) cf.generateCertificate(inStream);
			String info = x509Certificate.getSubjectDN().getName();
			logger.debug("证书拥有者:" + info);
		} catch (Exception e) {
			logger.error("证书加载异常." + e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					logger.error("关闭流异常." + e);
				}
			}
		}
		return x509Certificate;

	}

	public static X509CRL loadX509CRL(String httpCRL) {
		X509CRL crl = null;
		InputStream in = null;
		try {

			URL url = new URL(httpCRL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			in = conn.getInputStream();
			CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509);
			crl = (X509CRL) cf.generateCRL(in);

		} catch (Exception e) {
			logger.error("加载吊销列表异常." + e);
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				in = null;
				logger.error(e);
			}
		}
		return crl;
	}

	public static X509CRL loadX509CRL(File LocalFile) {
		X509CRL crl = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(LocalFile);
			CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509);
			crl = (X509CRL) cf.generateCRL(in);

		} catch (Exception e) {
			logger.error("加载吊销列表异常." + e);
		} finally {
			try {
				if (null != in) {
					in.close();
				}
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

	public static PrivateKey getPrivateKeyByBC(File pemFile) {
		PrivateKey privateKey = null;
		try {
			FileInputStream in = new FileInputStream(pemFile);

			// If the provided InputStream is encrypted, we need a password to
			// decrypt
			// it. If the InputStream is not encrypted, then the password is
			// ignored
			// (can be null). The InputStream can be DER (raw ASN.1) or PEM
			// (base64).
			DerValue arg0 = new DerValue(in);
			privateKey = PKCS8Key.parseKey(arg0);
			logger.debug(privateKey.getFormat());
			logger.debug(privateKey);
		} catch (Exception e) {
			logger.error(e);
		}

		// PKCS8Key pkcs8 = new PKCS8Key(in, "changeit".toCharArray() );
		//
		// // If an unencrypted PKCS8 key was provided, then this actually
		// returns
		// // exactly what was originally passed in (with no changes). If an
		// OpenSSL
		// // key was provided, it gets reformatted as PKCS #8 first, and so
		// these
		// // bytes will still be PKCS #8, not OpenSSL.
		// byte[] decrypted = pkcs8.getDecryptedBytes();
		// PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( decrypted );
		//
		// // A Java PrivateKey object is born.
		// PrivateKey pk = null;
		// if ( pkcs8.isDSA() )
		// {
		// pk = KeyFactory.getInstance( "DSA" ).generatePrivate( spec );
		// }
		// else if ( pkcs8.isRSA() )
		// {
		// pk = KeyFactory.getInstance( "RSA" ).generatePrivate( spec );
		// }
		//
		// // For lazier types:
		// pk = pkcs8.getPrivateKey();
		//
		return privateKey;
	}

	/**
	 * 输入参数是p8 格式的文件 openssl pkcs8 -topk8 -inform PEM -outform DER -in
	 * zhuyong001key.pem -out pkcs8_der.key -nocrypt
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年11月18日 下午5:12:39
	 * @param pemFile
	 * @return
	 */
	public static PrivateKey getPrivateKey(File pemFile) {

		PrivateKey privateKey = null;

		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(pemFile);
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			int len = 0;
			while (-1 != (len = fis.read(buffer))) {
				bos.write(buffer, 0, len);
			}

			KeyFactory kf = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bos.toByteArray());
			privateKey = kf.generatePrivate(keySpec);

		} catch (Exception e) {
			logger.error("加载私钥异常" + e);
		} finally {
			try {
				if (null != bos) {
					bos.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				logger.error("关闭流异常." + e);
			}
		}

		return privateKey;
	}

	public static PrivateKey getPrivateKey(byte[] privateBytes) {
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec priPKCS8;
		try {
			priPKCS8 = new PKCS8EncodedKeySpec(privateBytes);
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM_RSA);
			privateKey = keyf.generatePrivate(priPKCS8);
		} catch (Exception e) {
			logger.error("转换私钥异常." + e);
		}
		return privateKey;
	}
	
	public static PublicKey getPublicKey(byte[] publicKeyBytes) throws Exception {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
		return keyFactory.generatePublic(x509EncodedKeySpec);
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
	public static PrivateKey getPrivateKey(String keyStorePath, String keyStoreType, String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, keyStoreType, password);
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
	public static PublicKey getPublicKey(String certificatePath) throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		PublicKey key = certificate.getPublicKey();
		return key;
	}

	@SuppressWarnings("resource")
	public static void convetPCKS8ToPem(PrivateKey privateKey, String fileName) throws Exception {
		JcaPEMWriter privatepemWriter = new JcaPEMWriter(new FileWriter(new File(fileName)));
		privatepemWriter.writeObject(privateKey);
	}

	
	/**
	 * 获得Certificate
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static Certificate getCertificate(String certificatePath) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509);
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
	public static Certificate getCertificate(String keyStorePath, String keyStoreType, String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, keyStoreType, password);
		Certificate certificate = ks.getCertificate(alias);

		return certificate;
	}

	public static Provider getProvider(String keyStorePath, String keyStoreType, String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, keyStoreType, password);

		return ks.getProvider();
	}

	public static Certificate[] getCertificateChain(String keyStorePath, String keyStoreType, String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, keyStoreType, password);
		Certificate[] certificateChain = ks.getCertificateChain(alias);

		return certificateChain;
	}

	/**
	 * 获得KeyStore
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static KeyStore getKeyStore(String keyStorePath, String keyStoreType, String password) throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(keyStoreType);
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
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String keyStoreType, String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, keyStoreType, alias, password);

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
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String keyStoreType, String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, keyStoreType, alias, password);

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
	 * 私钥解密
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年12月4日 下午12:59:56
	 * @param data
	 * @param p8CertificatePath
	 * @return
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String p8CertificatePath) {
		byte[] ret = null;
		try {
			PrivateKey privateKey = getPrivateKey(new File(p8CertificatePath));

			// 对数据加密
			Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			ret = cipher.doFinal(data);
		} catch (Exception e) {
			logger.error("私钥解密异常." + e);
		}
		return ret;
	}

	/**
	 * 私钥解密
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年12月4日 下午12:49:56
	 * @param data
	 * @param privateKey
	 * @return
	 */
	public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) {
		byte[] ret = null;
		try {
			// 对数据加密
			Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			ret = cipher.doFinal(data);
		} catch (Exception e) {
			logger.error("私钥解密异常." + e);
		}
		return ret;
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
	public static String sign(byte[] sign, String keyStorePath, String keyStoreType, String alias, String password) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, keyStoreType, alias, password);
		// 获取私钥
		KeyStore ks = getKeyStore(keyStorePath, keyStoreType, password);
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
	public static boolean verifyCertificate(Date date, String keyStorePath, String keyStoreType, String alias, String password) {
		boolean status = true;
		try {
			Certificate certificate = getCertificate(keyStorePath, keyStoreType, alias, password);
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
	public static boolean verifyCertificate(String keyStorePath, String keyStoreType, String alias, String password) {
		return verifyCertificate(new Date(), keyStorePath, keyStoreType, alias, password);
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
	private static SSLSocketFactory getSSLSocketFactory(String password, String keyStorePath, String keyStoreType, String trustKeyStorePath) throws Exception {
		// 初始化密钥库
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SunX509);
		KeyStore keyStore = getKeyStore(keyStorePath, keyStoreType, password);
		keyManagerFactory.init(keyStore, password.toCharArray());

		// 初始化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(SunX509);
		KeyStore trustkeyStore = getKeyStore(trustKeyStorePath, keyStoreType, password);
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
	public static void configSSLSocketFactory(HttpsURLConnection conn, String password, String keyStorePath, String keyStoreType, String trustKeyStorePath) throws Exception {
		conn.setSSLSocketFactory(getSSLSocketFactory(password, keyStorePath, keyStoreType, trustKeyStorePath));
	}
}
