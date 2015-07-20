package com.zy.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;

import com.zy.security.jdk.CertificateCoder;

public class PKCSCertificateCoder extends Coder{
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static final String signatureAlgorithm_ = "";

	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		
		
//		testGenP12();
		testGenRootCA();
	}
	
	public static void testGenRootCA() throws Exception {
		
		//1.生成密钥对
		KeyPair keyPair = buildKeyPair();
		//打印原始私钥
		String privateStr1 = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
		
		//2.输出私钥pem文件
		storePrivatePem(new File("d:\\bc\\rootCAkey.pem"),keyPair.getPrivate(), "123456");
		//打印读取之前生成的私钥文件
		String privateStr2 = Base64.encodeBase64String(getPrivateKeyFromPem(FileUtils.readFileToString(new File("d:\\bc\\rootCAkey.pem")), "123456").getEncoded());
		
		//3.生成证书请求
		PKCS10CertificationRequest p10 = buildPKCS10(keyPair);
		
		//4.生成证书
		PublicKey publicKey = getPublicKeyFromPKCS10CertificationRequest(p10);
		X509Certificate caCert = buildCARootCertV3(publicKey,keyPair.getPrivate());
		
		//5.输出二进制证书文件
		FileUtils.writeByteArrayToFile(new File("d:\\bc\\rootCA.crt"), caCert.getEncoded());
		String cacerStr1 = Base64.encodeBase64String(caCert.getEncoded());
		
		//6.输出pem证书文件
		storePrivatePem(new File("d:\\bc\\rootCAcer.pem"), caCert, "");
		String cacerStr2 = Base64.encodeBase64String(CertificateCoder.getX509CertificateFromPem(FileUtils.readFileToString(new File("d:\\bc\\rootCAcer.pem"))).getEncoded());
		
		
		
		
		//验证转换前后是否正确
		System.out.println(privateStr1);
		System.out.println(privateStr2);
		System.out.println(cacerStr1);
		System.out.println(cacerStr2);
		
	}
	
	public static void testGenP12() throws Exception {
		//解析root CA 证书
		String rootcaCer = FileUtils.readFileToString(new File("d:\\rootcacer.pem"), "UTF-8");
		X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
		//解析root CA 私钥
		String rootcaKey = FileUtils.readFileToString(new File("d:\\rootcakey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = getPrivateKeyFromPem(rootcaKey,"123456");
		
		//1.生成用户密钥对
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();

		//2.生成用户证书请求
		PKCS10CertificationRequest p10 = buildPKCS10(kp);
		PublicKey publicKey = getPublicKeyFromPKCS10CertificationRequest(p10);
		
		//3.生成用户证书
		X509Certificate clientCertificate = buildEndEntityCert(publicKey,rootcaPrivateKey,rootcaCertificate);
		FileUtils.writeByteArrayToFile(new File("d:\\client.cer"), clientCertificate.getEncoded());
		
		//4.生成用户p12文件
		storeP12(kp, new X509Certificate[]{clientCertificate,rootcaCertificate},"d:\\client.p12", "123456");
				
	}
	
	/**
	 * 生成密钥对
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月17日 下午3:46:16
	 * @return
	 * @throws Exception
	 */
	public static KeyPair buildKeyPair() throws Exception {
		
		SecureRandom random = new SecureRandom();
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM_RSA, PROVIDER_BC);
		kpg.initialize(2048,random);
		KeyPair kp = kpg.genKeyPair();
		
		return kp;
	}
	
	/**
	 * 生成私钥pem格式文件,如果是写证书则不需要填写密码
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月17日 下午4:03:19
	 * @param privateKey
	 * @param password
	 * @param file
	 * @throws IOException
	 */
	public static void storePrivatePem(File file,Object obj,String password) throws Exception {
		String pem = writeObjToPem(obj,password);
		FileUtils.writeStringToFile(file, pem,CHARSET_UTF_8);
	}

	/**
	 * 生成证书请求
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月16日 下午2:49:39
	 * @param kp
	 * @return
	 * @throws Exception
	 */
	public static PKCS10CertificationRequest buildPKCS10(KeyPair kp) throws Exception{
		String sigName = "SHA1withRSA";
		
		X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE);
		x500NameBld.addRDN(BCStyle.C, "AU");
		x500NameBld.addRDN(BCStyle.ST, "Victoria");
		x500NameBld.addRDN(BCStyle.L, "Melbourne");
		x500NameBld.addRDN(BCStyle.O, "The Legion of the Bouncy Castle");
		x500NameBld.addRDN(BCStyle.CN, "zhuyong");
		X500Name subject = x500NameBld.build();
		
		PKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(subject, kp.getPublic());
		
//		ExtensionsGenerator extGen = new ExtensionsGenerator();
//		extGen.addExtension(Extension.subjectAlternativeName,false, new GeneralNames(new GeneralName(GeneralName.rfc822Name, "feedback-crypto@bouncycastle.org")));
//		
//		requestBuilder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,extGen.generate());
		
		PKCS10CertificationRequest p10 = requestBuilder.build(new JcaContentSignerBuilder(sigName).setProvider("BC").build(kp.getPrivate()));

		if (!p10.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider("BC").build(kp.getPublic()))) {
			System.out.println(sigName + ": Failed verify check.");
		} else {
			System.out.println(sigName + ": PKCS#10 request verified.");
		}
		return p10;
	}
	
	/**
	 * 从证书请求文件获取公钥
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月17日 下午4:12:33
	 * @param p10
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyFromPKCS10CertificationRequest(PKCS10CertificationRequest p10) throws Exception {
		SubjectPublicKeyInfo subPublicKeyInfo = p10.getSubjectPublicKeyInfo();
		RSAKeyParameters rsa = (RSAKeyParameters) PublicKeyFactory.createKey(subPublicKeyInfo);
		RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsa.getModulus(), rsa.getExponent());
		KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
		PublicKey publicKey = kf.generatePublic(rsaSpec);
		return publicKey;
	}
	
	/**
	 * 生成证书
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月17日 下午3:51:10
	 * @param entityKey
	 * @param caKey
	 * @param caCert
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate buildEndEntityCert(PublicKey entityKey, PrivateKey caKey, X509Certificate caCert) throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 10);
		
//		X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE);
//		x500NameBld.addRDN(BCStyle.C, "AU");
//		x500NameBld.addRDN(BCStyle.ST, "Victoria");
//		x500NameBld.addRDN(BCStyle.L, "Melbourne");
//		x500NameBld.addRDN(BCStyle.O, "The Legion of the Bouncy Castle");
//		X500Name subject = x500NameBld.build();
		
		
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(caCert.getSubjectX500Principal(),
				BigInteger.valueOf(1), 
				new Date(System.currentTimeMillis()), 
				c.getTime(), 
				new X500Principal("CN=zhuyong001,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN"), 
				entityKey);
		

        
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert))
				.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(entityKey))
				.addExtension(Extension.basicConstraints, true, new BasicConstraints(false))
				.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
		
		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(caKey);
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}
	
	/**
	 * 生成p12文件
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月16日 下午2:57:38
	 * @param kp
	 * @param chain [客户证书-中间CA-根CA]
	 * @param p12Path
	 * @param p12Password
	 * @return
	 */
	public static int storeP12(KeyPair kp,X509Certificate[] chain,String p12Path, String p12Password ) {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
			ks.load(null, null);
			ks.setKeyEntry("zhuyong",kp.getPrivate(), p12Password.toCharArray(), chain);
			FileOutputStream fOut = new FileOutputStream(p12Path);
			ks.store(fOut, p12Password.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * 生成V3 根CA
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月17日 下午4:55:51
	 * @param caPublicKey
	 * @param caPrivateKey
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate buildCARootCertV3(PublicKey caPublicKey,PrivateKey caPrivateKey) throws Exception {
		X500Name issuer = new X500Name("CN=Test Root Certificate,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
		BigInteger serial = BigInteger.valueOf(1) ;
		Date notBefore = new Date();
		Date notAfter = sdf.parse("2017-07-07 07:07:07");
		X500Name subject = new X500Name("CN=Test Root Certificate,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
		PublicKey publicKey = caPublicKey;
		
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKey);
		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(caPrivateKey);
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}
	
	/**
	 * 生成V3 根CA
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月16日 下午3:32:48
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate buildCARootCertV3(KeyPair keyPair) throws Exception {
		X500Name issuer = new X500Name("CN=Test Root Certificate,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
		BigInteger serial = BigInteger.valueOf(1) ;
		Date notBefore = new Date();
		Date notAfter = sdf.parse("2017-07-07 07:07:07");
		X500Name subject = new X500Name("CN=Test Root Certificate,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
		PublicKey publicKey = keyPair.getPublic();
		
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKey);
		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(keyPair.getPrivate());
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}
	
	/**
	 * 把对象写成pem格式文件
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月17日 下午5:06:40
	 * @param obj  可以是PrivateKey ,X509Certificate ,or PublicKey(一般不用这种情况)
	 * @return
	 * @throws IOException
	 */
	public static String writeObjToPem(Object obj,String password) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));

		if(obj instanceof PrivateKey) {
			if(null!= password && !"".equals(password.trim())) {
				JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(PKCS8Generator.PBE_SHA1_3DES);
			    encryptorBuilder.setRandom(new SecureRandom());
			    encryptorBuilder.setPasssword(password.toCharArray());
			    OutputEncryptor oe = encryptorBuilder.build();
			    JcaPKCS8Generator gen = new JcaPKCS8Generator((PrivateKey)obj,oe);
			    PemObject pemObject = gen.generate();
			    obj = pemObject;
		    }
		    pemWriter.writeObject(obj);
		}else if(obj instanceof X509Certificate) {
			pemWriter.writeObject(obj);
		}else {
			pemWriter.writeObject(obj);
		}
		

		pemWriter.close();

		return new String(bos.toByteArray());
	}

	public static PrivateKey getPrivateKeyFromPem(String der,String password) throws Exception {
		StringReader reader = new StringReader(der);
		PEMParser pemParser = new PEMParser(reader);
		PrivateKey privateKey = null;
		try {
			Object o = pemParser.readObject();
			if(o instanceof PrivateKeyInfo) {
				privateKey = new JcaPEMKeyConverter().setProvider(PROVIDER_BC).getPrivateKey((PrivateKeyInfo)o);
			}else if(o instanceof PEMKeyPair) {
				privateKey = new JcaPEMKeyConverter().setProvider(PROVIDER_BC).getKeyPair((PEMKeyPair) o).getPrivate();
			}else if(o instanceof PEMEncryptedKeyPair) {
				PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
				PrivateKeyInfo privateKeyInfo = ((PEMEncryptedKeyPair)o).decryptKeyPair(decProv).getPrivateKeyInfo();
				
				privateKey = new JcaPEMKeyConverter().setProvider(PROVIDER_BC).getPrivateKey(privateKeyInfo);
			}else if(o instanceof PKCS8EncryptedPrivateKeyInfo) {
				PKCS8EncryptedPrivateKeyInfo pkcs8EncryptedPrivateKeyInfo = (PKCS8EncryptedPrivateKeyInfo)o;
				JceOpenSSLPKCS8DecryptorProviderBuilder jce = new JceOpenSSLPKCS8DecryptorProviderBuilder();
                InputDecryptorProvider decProv = jce.build(password.toCharArray());
                PrivateKeyInfo privateKeyInfo = pkcs8EncryptedPrivateKeyInfo.decryptPrivateKeyInfo(decProv);
                
                privateKey = new JcaPEMKeyConverter().setProvider(PROVIDER_BC).getPrivateKey(privateKeyInfo);
			}else {
				throw new RuntimeException("没用找到数据类型[" + o.getClass() + "]");
			}
		}finally {
			pemParser.close();
		}
		return privateKey;
	}
	
}
