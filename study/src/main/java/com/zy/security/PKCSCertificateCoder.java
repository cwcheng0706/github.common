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
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.BasicOCSPRespBuilder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.OCSPRespBuilder;
import org.bouncycastle.cert.ocsp.Req;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.PrincipalUtil;
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
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
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
		
//		testGenP12ForAtlantis();
		
		
		
//		testGenRootCA("client");
		testGenClient("client");
		testGenCRL("client");
		
//		testGenRootCA("server");
//		testGenServer("server");
		
		
//		testGenRootCA("ocsp");
//		testCreateOCSP("ocsp");
		
		
	}
	
	public static void testGenCRL(String rootAlias) throws Exception {
		//解析root CA   证书 --可能专门生成一个CRL CA级别的根证书
		String rootcaCer = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAcer.pem"), "UTF-8");
		X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
		//解析root CA 私钥
		String rootcaKey = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAkey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = getPrivateKeyFromPem(rootcaKey,"123456");
		
		//生成CRL
//		X509CRL crl = generateCrl(rootcaCertificate,rootcaPrivateKey);
//		FileUtils.writeByteArrayToFile(new File("d:\\bc\\crl.crl"), crl.getEncoded());
//		storeX509CRLPem(new File("d:\\bc\\crl.pem"),crl);
		
		X509Certificate cert = CertificateCoder.getX509CertificateFromPem(FileUtils.readFileToString(new File("d:\\bc\\clientcer.pem")));
		revoke(cert , new File("d:\\bc\\crl.crl"), rootcaPrivateKey);
	}
	
	public static void testCreateOCSP(String rootAlias) throws Exception {
		//解析root CA 证书
		String rootcaCer = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAcer.pem"), "UTF-8");
		X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
		//解析root CA 私钥
		String rootcaKey = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAkey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = getPrivateKeyFromPem(rootcaKey,"123456");
		
		
		X509Certificate certificate = CertificateCoder.getX509CertificateFromPem(FileUtils.readFileToString(new File("d:\\bc\\clientcer.pem")));
		X509Certificate issuerCertificate = CertificateCoder.getX509CertificateFromPem(FileUtils.readFileToString(new File("d:\\bc\\clientRootCAcer.pem")));
		OCSPResp resp = createOcspResp(certificate, true, issuerCertificate, rootcaCertificate, rootcaPrivateKey, Long.valueOf("2"));
		System.out.println(resp.getStatus());
	}

	public static void testGenRootCA(String alias) throws Exception {
		
		//1.生成密钥对
		KeyPair keyPair = buildKeyPair();
		//打印原始私钥
		String privateStr1 = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
		
		//2.输出私钥pem文件
		storePrivatePem(new File("d:\\bc\\" + alias + "RootCAkey.pem"),keyPair.getPrivate(), "123456");
		//打印读取之前生成的私钥文件
		String privateStr2 = Base64.encodeBase64String(getPrivateKeyFromPem(FileUtils.readFileToString(new File("d:\\bc\\" + alias + "RootCAkey.pem")), "123456").getEncoded());
		
		//3.生成证书请求
		PKCS10CertificationRequest p10 = buildPKCS10(keyPair);
		
		//4.生成证书
		PublicKey publicKey = getPublicKeyFromPKCS10CertificationRequest(p10);
		X509Certificate caCert = buildCARootCertV3(publicKey,keyPair.getPrivate());
//		System.out.println();
//		System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));
//		System.out.println(Base64.encodeBase64String(caCert.getPublicKey().getEncoded()));
//		PublicKey p1 = CertificateCoder.getPublicKey(publicKey.getEncoded());
//		System.out.println(Base64.encodeBase64String(p1.getEncoded()));
//		System.out.println(Base64.encodeBase64String(caCert.getEncoded()));
//		System.out.println();
		
		//5.输出证书二进制文件
		FileUtils.writeByteArrayToFile(new File("d:\\bc\\" + alias + "RootCA.crt"), caCert.getEncoded());
		String cacerStr1 = Base64.encodeBase64String(caCert.getEncoded());
		
		//6.输出证书pem文件
		storeCertificatePem(new File("d:\\bc\\" + alias + "RootCAcer.pem"), caCert);
		String cacerStr2 = Base64.encodeBase64String(CertificateCoder.getX509CertificateFromPem(FileUtils.readFileToString(new File("d:\\bc\\" + alias + "RootCAcer.pem"))).getEncoded());
		
		
		
		
		//验证转换前后是否正确
		System.out.println(privateStr1);
		System.out.println(privateStr2);
		System.out.println(cacerStr1);
		System.out.println(cacerStr2);
		
	}
	
	public static void testGenClient(String rootAlias) throws Exception {
		//解析root CA 证书
		String rootcaCer = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAcer.pem"), "UTF-8");
		X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
		//解析root CA 私钥
		String rootcaKey = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAkey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = getPrivateKeyFromPem(rootcaKey,"123456");
		
		//1.生成用户密钥对
		KeyPair keyPair = buildKeyPair();
		
		//2.输出私钥文件
		storePrivatePem(new File("d:\\bc\\clientkey.pem"),keyPair.getPrivate(), "123456");

		//3.生成用户证书请求
		PKCS10CertificationRequest p10 = buildPKCS10(keyPair);
		PublicKey publicKey = getPublicKeyFromPKCS10CertificationRequest(p10);
		
		//4.生成用户证书 二进制文件和pem文件
		X509Certificate clientCertificate = buildEndEntityCert(publicKey,rootcaPrivateKey,rootcaCertificate);
		FileUtils.writeByteArrayToFile(new File("d:\\bc\\client.cer"), clientCertificate.getEncoded());
		storeCertificatePem(new File("d:\\bc\\clientcer.pem"), clientCertificate);
		
		//5.生成用户p12文件
		storeP12(keyPair, new X509Certificate[]{clientCertificate,rootcaCertificate},"d:\\bc\\client.p12", "123456");
		
	}
	
	public static void testGenServer(String rootAlias) throws Exception {
		//解析root CA 证书
		String rootcaCer = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAcer.pem"), "UTF-8");
		X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
		//解析root CA 私钥
		String rootcaKey = FileUtils.readFileToString(new File("d:\\bc\\" + rootAlias + "RootCAkey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = getPrivateKeyFromPem(rootcaKey,"123456");
		
		//1.生成用户密钥对
		KeyPair keyPair = buildKeyPair();
		
		//2.输出私钥文件
		storePrivatePem(new File("d:\\bc\\serverkey.pem"),keyPair.getPrivate(), "123456");

		//3.生成用户证书请求
		PKCS10CertificationRequest p10 = buildPKCS10(keyPair);
		PublicKey publicKey = getPublicKeyFromPKCS10CertificationRequest(p10);
		
		//4.生成用户证书 二进制文件和pem文件
		X509Certificate clientCertificate = buildEndEntityCert(publicKey,rootcaPrivateKey,rootcaCertificate);
		FileUtils.writeByteArrayToFile(new File("d:\\bc\\server.cer"), clientCertificate.getEncoded());
		storeCertificatePem(new File("d:\\bc\\servercer.pem"), clientCertificate);
		
		//5.生成用户p12文件
		storeP12(keyPair, new X509Certificate[]{clientCertificate,rootcaCertificate},"d:\\bc\\server.p12", "123456");
	}
	
	public static void testGenP12ForAtlantis() throws Exception {
		//解析root CA 证书
		String rootcaCer = FileUtils.readFileToString(new File("d:\\rootcacer.pem"), "UTF-8");
		X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
		//解析root CA 私钥
		String rootcaKey = FileUtils.readFileToString(new File("d:\\rootcakey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = getPrivateKeyFromPem(rootcaKey,"123456");
		
		//1.生成用户密钥对
		KeyPair keyPair = buildKeyPair();
		
		//2.输出私钥文件
		storePrivatePem(new File("d:\\clientkey.pem"),keyPair.getPrivate(), "123456");

		//3.生成用户证书请求
		PKCS10CertificationRequest p10 = buildPKCS10(keyPair);
		PublicKey publicKey = getPublicKeyFromPKCS10CertificationRequest(p10);
		
		//4.生成用户证书 二进制文件和pem文件
		X509Certificate clientCertificate = buildEndEntityCert(publicKey,rootcaPrivateKey,rootcaCertificate);
		FileUtils.writeByteArrayToFile(new File("d:\\client.cer"), clientCertificate.getEncoded());
		storePrivatePem(new File("d:\\clientkey.pem"), keyPair.getPrivate(), "123456");
		
		//5.生成用户p12文件
		storeP12(keyPair, new X509Certificate[]{clientCertificate,rootcaCertificate},"d:\\client.p12", "123456");
				
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
	 * 生成私钥pem格式文件
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月21日 上午11:15:45
	 * @param file
	 * @param privateKey  PrivateKey
	 * @param password
	 * @throws Exception
	 */
	public static void storePrivatePem(File file,PrivateKey privateKey,String password) throws Exception {
		String pem = writeObjToPem(privateKey,password);
		FileUtils.writeStringToFile(file, pem,CHARSET_UTF_8);
	}
	
	/**
	 * 将证书写入pem文件
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月21日 上午10:52:03
	 * @param file
	 * @param x509Certificate
	 * @throws Exception
	 */
	public static void storeCertificatePem(File file,X509Certificate x509Certificate) throws Exception {
		String pem = writeObjToPem(x509Certificate,null);
		FileUtils.writeStringToFile(file, pem,CHARSET_UTF_8);
	}
	
	/**
	 * 将吊销文件写入pem文件
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月22日 下午2:33:26
	 * @param file
	 * @param x509crl
	 * @throws Exception
	 */
	public static void storeX509CRLPem(File file,X509CRL x509crl) throws Exception {
		String pem = writeObjToPem(x509crl, null);
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
		//CN=zhuyong001,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN
		X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE);
		x500NameBld.addRDN(BCStyle.C, "CN");
		x500NameBld.addRDN(BCStyle.ST, "SH");
		x500NameBld.addRDN(BCStyle.L, "SH_L");
		x500NameBld.addRDN(BCStyle.O, "JL");
		x500NameBld.addRDN(BCStyle.OU, "JL");
		x500NameBld.addRDN(BCStyle.CN, "zhuyong001");
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
	 * @param publicKey
	 * @param caKey
	 * @param caCert
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate buildEndEntityCert(PublicKey publicKey, PrivateKey caKey, X509Certificate caCert) throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 10);
		
//		X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE);
//		x500NameBld.addRDN(BCStyle.C, "AU");
//		x500NameBld.addRDN(BCStyle.ST, "Victoria");
//		x500NameBld.addRDN(BCStyle.L, "Melbourne");
//		x500NameBld.addRDN(BCStyle.O, "The Legion of the Bouncy Castle");
//		X500Name subject = x500NameBld.build();
		
		
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(caCert.getSubjectX500Principal(),
				BigInteger.valueOf(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()), 
				c.getTime(), 
				new X500Principal("CN=zhuyong001,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN"), 
				publicKey);
		

        
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert))
				.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(publicKey))
				//.addExtension(Extension.basicConstraints, true, new BasicConstraints(false))
				//.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment))
				//改成false证书里面就不会用感叹号了
				.addExtension(Extension.basicConstraints, false, new BasicConstraints(false))
				.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment))
				;
		
		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(caKey);
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}
	
	/**
	 * 生成中间证书 带有CA功能
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月21日 下午5:26:22
	 * @param publicKey
	 * @param caKey
	 * @param caCert
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate buildIntermediateCert(PublicKey publicKey, PrivateKey caKey, X509Certificate caCert) throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 10);
		
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(caCert.getSubjectX500Principal(),
				BigInteger.valueOf(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()), 
				c.getTime(), 
				new X500Principal("CN=zhuyong001,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN"), 
				publicKey);
		
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert))
				.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(publicKey))
				.addExtension(Extension.basicConstraints, true, new BasicConstraints(0))
				.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
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
		X500Name issuer = new X500Name("CN=CA ZY Root Certificate Test,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
		BigInteger serial = BigInteger.valueOf(1) ;
		Date notBefore = new Date();
		Date notAfter = sdf.parse("2017-07-07 07:07:07");
		X500Name subject = new X500Name("CN=CA ZY Root Certificate Test,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
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
	 * 把对象写成pem格式文件,写证书时不用填写password
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
	
	/**
	 * 生成吊销列表
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月22日 下午3:00:25
	 * @param issuerCertificate
	 * @param issuerPrivateKey
	 * @return
	 * @throws Exception
	 */
	public static X509CRL generateCrl(X509Certificate issuerCertificate, PrivateKey issuerPrivateKey) throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 10);
		
		X509CertificateHolder holder = new X509CertificateHolder(issuerCertificate.getEncoded());
        X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(holder.getIssuer(), new Date());
        crlBuilder.setNextUpdate(new Date(new Date().getTime() + 100000));
        JcaContentSignerBuilder contentBuilder = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC");

        CRLNumber crlNumber = new CRLNumber(new BigInteger("2"));
       
        crlBuilder.addExtension(Extension.cRLNumber, false, crlNumber);
        X509CRLHolder x509Crl = crlBuilder.build(contentBuilder.build(issuerPrivateKey));
        return new JcaX509CRLConverter().setProvider("BC").getCRL(x509Crl);
	}
	
	public static boolean revoke(X509Certificate cert, File caRevocationList, PrivateKey caPrivateKey) {
		try {
			X500Name issuerDN = new X500Name(PrincipalUtil.getIssuerX509Principal(cert).getName());
			X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(issuerDN, new Date());
			if (caRevocationList.exists()) {
				byte[] data = FileUtils.readFileToByteArray(caRevocationList);
				X509CRLHolder crl = new X509CRLHolder(data);
				crlBuilder.addCRL(crl);
			}

			crlBuilder.addCRLEntry(cert.getSerialNumber(), new Date(), CRLReason.privilegeWithdrawn);//这里可以填其它原因
//			Extension extension = new Extension(extnId, critical, value);
//			Extensions extensions = new Extensions(extension);
//			crlBuilder.addCRLEntry(cert.getSerialNumber(), new Date(), extensions);

			// build and sign CRL with CA private key
			ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSA").setProvider(PROVIDER_BC).build(caPrivateKey);
			X509CRLHolder crl = crlBuilder.build(signer);

			File tmpFile = new File(caRevocationList.getParentFile(), Long.toHexString(System.currentTimeMillis()) + ".tmp");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(tmpFile);
				fos.write(crl.getEncoded());
				fos.flush();
				fos.close();
				if (caRevocationList.exists()) {
					caRevocationList.delete();
				}
				tmpFile.renameTo(caRevocationList);

			} finally {
				if (fos != null) {
					fos.close();
				}
				if (tmpFile.exists()) {
					tmpFile.delete();
				}
			}

//			x509log.log(MessageFormat.format("Revoked certificate {0,number,0} reason: {1} [{2}]", cert.getSerialNumber(), reason.toString(), cert.getSubjectDN().getName()));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static OCSPResp createOcspResp(X509Certificate certificate, boolean revoked, X509Certificate issuerCertificate, X509Certificate ocspResponderCertificate,
			PrivateKey ocspResponderPrivateKey, long nonceTimeinMillis) throws Exception {
		DigestCalculator digestCalc = new JcaDigestCalculatorProviderBuilder().setProvider("BC").build().get(CertificateID.HASH_SHA1);
		X509CertificateHolder issuerHolder = new X509CertificateHolder(issuerCertificate.getEncoded());
		CertificateID certId = new CertificateID(digestCalc, issuerHolder, certificate.getSerialNumber());

		// request
		// create a nonce to avoid replay attack
		BigInteger nonce = BigInteger.valueOf(nonceTimeinMillis);
		DEROctetString nonceDer = new DEROctetString(nonce.toByteArray());
		Extension ext = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, true, nonceDer);
		Extensions exts = new Extensions(ext);

		OCSPReqBuilder ocspReqBuilder = new OCSPReqBuilder();
		ocspReqBuilder.addRequest(certId);
		ocspReqBuilder.setRequestExtensions(exts);
		OCSPReq ocspReq = ocspReqBuilder.build();

		SubjectPublicKeyInfo keyInfo = new SubjectPublicKeyInfo(CertificateID.HASH_SHA1, ocspResponderCertificate.getPublicKey().getEncoded());

		BasicOCSPRespBuilder basicOCSPRespBuilder = new BasicOCSPRespBuilder(keyInfo, digestCalc);
		basicOCSPRespBuilder.setResponseExtensions(exts);

		// request processing
		Req[] requestList = ocspReq.getRequestList();
		for (Req ocspRequest : requestList) {
			CertificateID certificateID = ocspRequest.getCertID();
			CertificateStatus certificateStatus = CertificateStatus.GOOD;
			if (revoked) {
				certificateStatus = new RevokedStatus(new Date(), CRLReason.privilegeWithdrawn);
			}
			basicOCSPRespBuilder.addResponse(certificateID, certificateStatus);
		}

		// basic response generation
		X509CertificateHolder[] chain = null;
		if (!ocspResponderCertificate.equals(issuerCertificate)) {
			// TODO: HorribleProxy can't convert array input params yet
			chain = new X509CertificateHolder[] { new X509CertificateHolder(ocspResponderCertificate.getEncoded()), issuerHolder };
		}

		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(ocspResponderPrivateKey);
		BasicOCSPResp basicOCSPResp = basicOCSPRespBuilder.build(contentSigner, chain, new Date(nonceTimeinMillis));

		OCSPRespBuilder ocspRespBuilder = new OCSPRespBuilder();
		OCSPResp ocspResp = ocspRespBuilder.build(OCSPRespBuilder.SUCCESSFUL, basicOCSPResp);

		return ocspResp;
	}
	
	
	public static enum RevocationReason {
		// https://en.wikipedia.org/wiki/Revocation_list
		unspecified, keyCompromise, caCompromise, affiliationChanged, superseded, cessationOfOperation, certificateHold, unused, removeFromCRL, privilegeWithdrawn, ACompromise;

		public static RevocationReason[] reasons = { unspecified, keyCompromise, caCompromise, affiliationChanged, superseded, cessationOfOperation, privilegeWithdrawn };

		@Override
		public String toString() {
			return name() + " (" + ordinal() + ")";
		}
	}
}
