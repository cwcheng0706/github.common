package com.zy.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS12PfxPdu;
import org.bouncycastle.pkcs.PKCS12PfxPduBuilder;
import org.bouncycastle.pkcs.PKCS12SafeBag;
import org.bouncycastle.pkcs.PKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEOutputEncryptorBuilder;

import com.zy.security.jdk.CertificateCoder;

public class ClientCertGenerator {
	
	private static final long VALIDITY_PERIOD = 10000000;

	private static final char[] KEY_PASSWD = "rootca123456".toCharArray();
	
	private static String PRIVATE_PREFIX = "-----BEGIN RSA PRIVATE KEY-----\n";
	private static String PRIVATE_SUFFIX = "-----END RSA PRIVATE KEY-----\n";
	
	private static String private_prefix_des3 = "-----BEGIN RSA PRIVATE KEY-----\nProc-Type: 4,ENCRYPTED\nDEK-Info: DES-EDE3-CBC,1B397661A3729FC4\r\n";
	private static String private_suffix_des3 = "-----END RSA PRIVATE KEY-----\n";
	
	private static String PUBLIC_PREFIX = "-----BEGIN CERTIFICATE-----\n";
	private static String PUBLIC_SUFFIX = "-----END CERTIFICATE-----\n";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static void main(String[] args) throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		testcaV1();
		testGenP12();
		//testcaV3();
	}

	/**
	 * Build a sample V1 certificate to use as a CA root certificate
	 */
	public static X509Certificate buildCARootCertV1(KeyPair keyPair) throws Exception {
		X500Name issuer = new X500Name("CN=Test Root Certificate,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
		BigInteger serial = BigInteger.valueOf(1) ;
		Date notBefore = new Date();
		Date notAfter = sdf.parse("2016-06-06 06:06:06");
		X500Name subject = new X500Name("CN=Test Root Certificate,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN");
		PublicKey publicKey = keyPair.getPublic();
		
		X509v1CertificateBuilder certBldr = new JcaX509v1CertificateBuilder(issuer, serial,notBefore,notAfter,subject,publicKey);
		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(keyPair.getPrivate());
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}
	
	public static void testcaV1() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");   
        kpg.initialize(2048);   
        KeyPair keyPair = kpg.generateKeyPair();   
        X509Certificate cer = buildCARootCertV1(keyPair);
        System.out.println(cer);
        FileUtils.writeByteArrayToFile(new File("d:\\caV1cer.cer"), cer.getEncoded());
        //生成CA证书
        byte[] publicBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(cer.getEncoded(),true);
        String publicStr = new String(publicBase64,"UTF-8");
        publicStr = PUBLIC_PREFIX + publicStr + PUBLIC_SUFFIX;
        System.out.println(publicStr);
        FileUtils.writeByteArrayToFile(new File("d:\\caV1cer.pem"), publicStr.getBytes("UTF-8")); 	
        
        //生成CA私钥
        byte[] privateBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(keyPair.getPrivate().getEncoded(),true);
        String privateStr = new String(privateBase64,"UTF-8");
        privateStr = PRIVATE_PREFIX + privateStr + PRIVATE_SUFFIX;
        System.out.println(privateStr);
        FileUtils.writeByteArrayToFile(new File("d:\\caV1key.pem"), privateStr.getBytes("UTF-8")); 	
        
        
	}
	
	/**
	 * Build a sample V3 certificate to use as a CA root certificate
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
	
	public static void testcaV3() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");   
        kpg.initialize(2048);   
        KeyPair keyPair = kpg.generateKeyPair();   
        X509Certificate cer = buildCARootCertV3(keyPair);
        FileUtils.writeByteArrayToFile(new File("d:\\caV3.cer"), cer.getEncoded());
        System.out.println(cer);
	}


	/**
	 * Build a sample V3 certificate to use as an intermediate CA certificate
	 */
	public static X509Certificate buildIntermediateCert(PublicKey intKey, PrivateKey caKey, X509Certificate caCert) throws Exception {
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(caCert.getSubjectX500Principal(), BigInteger.valueOf(1), new Date(), sdf.parse("2016-07-06 06:06:06"), new X500Principal("CN=Test CA Certificate"), intKey);
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert))
				.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(intKey)).addExtension(Extension.basicConstraints, true, new BasicConstraints(0))
				.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
		ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(caKey);
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBldr.build(signer));
	}
	


	/**
	 * Build a sample V3 certificate to use as an end entity certificate
	 */
	public static X509Certificate buildEndEntityCert(PublicKey entityKey, PrivateKey caKey, X509Certificate caCert) throws Exception {
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(caCert.getSubjectX500Principal(), 
				BigInteger.valueOf(1), 
				new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis() + VALIDITY_PERIOD), 
				new X500Principal("CN=Test End Entity Certificate"), 
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
	 * Build a sample V1 certificate to use as a CA root certificate
	 */
//	public static X509CertificateHolder buildRootCert(AsymmetricCipherKeyPair keyPair) throws Exception {
//		X509v1CertificateBuilder certBldr = new X509v1CertificateBuilder(new X500Name("CN=Test Root Certificate"), BigInteger.valueOf(1), new Date(System.currentTimeMillis()), new Date(
//				System.currentTimeMillis() + VALIDITY_PERIOD), new X500Name("CN=Test Root Certificate"),
//
//		SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(keyPair.getPublic()));
//		AlgorithmIdentifier sigAlg = algFinder.find("SHA1withRSA");
//		AlgorithmIdentifier digAlg = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlg);
//		ContentSigner signer = new BcRSAContentSignerBuilder(sigAlg, digAlg).build(keyPair.getPrivate());
//		return certBldr.build(signer);
//	}

	/**
	 * Build a sample V3 certificate to use as an intermediate CA certificate
	 */
//	public static X509CertificateHolder buildIntermediateCert(AsymmetricKeyParameter intKey, AsymmetricKeyParameter caKey, X509CertificateHolder caCert) throws Exception {
//		SubjectPublicKeyInfo intKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(intKey);
//		X509v3CertificateBuilder certBldr = new X509v3CertificateBuilder(caCert.getSubject(), BigInteger.valueOf(1), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()
//				+ VALIDITY_PERIOD), new X500Name("CN=Test CA Certificate"), intKeyInfo);
//		X509ExtensionUtils extUtils = new X509ExtensionUtils(new SHA1DigestCalculator());
//		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert))
//				.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(intKeyInfo)).addExtension(Extension.basicConstraints, true, new BasicConstraints(0))
//				.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
//		AlgorithmIdentifier sigAlg = algFinder.find("SHA1withRSA");
//		AlgorithmIdentifier digAlg = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlg);
//		ContentSigner signer = new BcRSAContentSignerBuilder(sigAlg, digAlg).build(caKey);
//		return certBldr.build(signer);
//	}

	/**
	 * Build a sample V3 certificate to use as an end entity certificate
	 */
//	public static X509CertificateHolder buildEndEntityCert(AsymmetricKeyParameter entityKey, AsymmetricKeyParameter caKey, X509CertificateHolder caCert) throws Exception {
//		SubjectPublicKeyInfo entityKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(entityKey);
//		X509v3CertificateBuilder certBldr = new X509v3CertificateBuilder(caCert.getSubject(), BigInteger.valueOf(1), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()
//				+ VALIDITY_PERIOD), new X500Name("CN=Test End Entity Certificate"), entityKeyInfo);
//		X509ExtensionUtils extUtils = new X509ExtensionUtils(new SHA1DigestCalculator());
//		certBldr.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert))
//				.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(entityKeyInfo)).addExtension(Extension.basicConstraints, true, new BasicConstraints(false))
//				.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
//		AlgorithmIdentifier sigAlg = algFinder.find("SHA1withRSA");
//		AlgorithmIdentifier digAlg = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlg);
//		ContentSigner signer = new BcRSAContentSignerBuilder(sigAlg, digAlg).build(caKey);
//		return certBldr.build(signer);
//	}
	
	public static PKCS10CertificationRequest genPKCS10(KeyPair kp) throws Exception{
		String sigName = "SHA1withRSA";
		
		X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE);
		x500NameBld.addRDN(BCStyle.C, "AU");
		x500NameBld.addRDN(BCStyle.ST, "Victoria");
		x500NameBld.addRDN(BCStyle.L, "Melbourne");
		x500NameBld.addRDN(BCStyle.O, "The Legion of the Bouncy Castle");
		X500Name subject = x500NameBld.build();
		
		PKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(subject, kp.getPublic());
		
		ExtensionsGenerator extGen = new ExtensionsGenerator();
		extGen.addExtension(Extension.subjectAlternativeName,false, new GeneralNames(new GeneralName(GeneralName.rfc822Name, "feedback-crypto@bouncycastle.org")));
		
		requestBuilder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,extGen.generate());
		
		PKCS10CertificationRequest p10 = requestBuilder.build(new JcaContentSignerBuilder(sigName).setProvider("BC").build(kp.getPrivate()));

		if (!p10.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider("BC").build(kp.getPublic()))) {
			System.out.println(sigName + ": Failed verify check.");
		} else {
			System.out.println(sigName + ": PKCS#10 request verified.");
		}
		return p10;
	}
	
	public static void genPKCS12File(OutputStream pfxOut, PrivateKey key, Certificate[] chain) throws Exception {
		
		OutputEncryptor encOut = new JcePKCSPBEOutputEncryptorBuilder(NISTObjectIdentifiers.id_aes256_CBC).setProvider("BC").build(KEY_PASSWD);
		
		PKCS12SafeBagBuilder taCertBagBuilder = new JcaPKCS12SafeBagBuilder((X509Certificate)chain[2]);
		taCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new DERBMPString("Bouncy Primary Certificate"));
		
//		PKCS12SafeBagBuilder caCertBagBuilder = new JcaPKCS12SafeBagBuilder((X509Certificate)chain[1]);
//		caCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new DERBMPString("Bouncy Intermediate Certificate"));
		
		PKCS12SafeBagBuilder eeCertBagBuilder = new JcaPKCS12SafeBagBuilder((X509Certificate)chain[0]);
		eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new DERBMPString("Eric's Key"));
		
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
		SubjectKeyIdentifier pubKeyId = extUtils.createSubjectKeyIdentifier(chain[0].getPublicKey());
		eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.localKeyIdAttribute, pubKeyId);
		
		PKCS12SafeBagBuilder keyBagBuilder = new JcaPKCS12SafeBagBuilder(key, encOut);
		keyBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new DERBMPString("Eric's Key"));
		keyBagBuilder.addBagAttribute(PKCS12SafeBag.localKeyIdAttribute, pubKeyId);
		
		PKCS12PfxPduBuilder builder = new PKCS12PfxPduBuilder();
		builder.addData(keyBagBuilder.build());
		builder.addEncryptedData(new JcePKCSPBEOutputEncryptorBuilder(PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC).setProvider("BC").build(KEY_PASSWD), 
				                 new PKCS12SafeBag[]{eeCertBagBuilder.build(), 
													 //caCertBagBuilder.build(),
													 taCertBagBuilder.build()});
		PKCS12PfxPdu pfx = builder.build(new JcePKCS12MacCalculatorBuilder(NISTObjectIdentifiers.id_sha256), KEY_PASSWD);
		// make sure we don't include indefinite length encoding
		pfxOut.write(pfx.getEncoded(ASN1Encoding.DL));
		pfxOut.close();
	}
	
//	public java.security.cert.Certificate certToX509Cert(X509Certificate cert) {
//		try {
//			CertificateFactory cf = new CertificateFactory();
//			InputStream is = new ByteArrayInputStream(cert.getEncoded());
//			Collection coll = cf.engineGenerateCertificates(is);
//			java.security.cert.Certificate jcrt = null;
//			Iterator it = coll.iterator(); 
//			if (it.hasNext()) {
//				jcrt = (java.security.cert.Certificate) it.next(); 
//				return jcrt;
//			}
//		} catch (CertificateEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CertificateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

	
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

	
	
	
	
	public static void testGenP12() throws Exception {
		
		
		String rootCerBase64 = "MIIDvTCCAqWgAwIBAgIEEioP6zANBgkqhkiG9w0BAQsFADCBjjELMAkGA1UEBhMC" +
								"Q04xETAPBgNVBAgTCHNoYW5naGFpMREwDwYDVQQHEwhzaGFuZ2hhaTEzMDEGA1UE" +
								"Cgwq5LiK5rW36YeR6bm/6YeR6J6N5L+h5oGv5pyN5Yqh5pyJ6ZmQ5YWs5Y+4MQsw" +
								"CQYDVQQLEwJJVDEXMBUGA1UEAxMOb3Blbi5qbGZleC5jb20wHhcNMTQwODIxMDM0" +
								"NTQ5WhcNMjQwODE4MDM0NTQ5WjCBjjELMAkGA1UEBhMCQ04xETAPBgNVBAgTCHNo" +
								"YW5naGFpMREwDwYDVQQHEwhzaGFuZ2hhaTEzMDEGA1UECgwq5LiK5rW36YeR6bm/" +
								"6YeR6J6N5L+h5oGv5pyN5Yqh5pyJ6ZmQ5YWs5Y+4MQswCQYDVQQLEwJJVDEXMBUG" +
								"A1UEAxMOb3Blbi5qbGZleC5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK" +
								"AoIBAQCQ4q4Yh8EPHbAP+BMiystXOEV56OE+IUwxSS7fRZ3ZrIEPImpnCiAe1txZ" +
								"vk0Lgv8ZWqrj4ErOT5FoOWfQW6Vva1DOXknCzFbypJMjqVnIS1/OwB64sYg4naLc" +
								"mM95GAHtEv9qxIWLbPhoLShz54znRNbM7mZJyT4BwLhKuKmfdo3UEuXvcoUFLN2l" +
								"f2wiTNmgMgpxcnCsWAx2nJaonPGCVXeQu0PXCVmCTyUUCWdT7P1io5yEpuRP/Dac" +
								"//g7Em8rkulgeO7e3gnEbrgrczsr2H1KJLTBjQmyWeWZg7LRYML6oHODrrDb0x++" +
								"yDT01p2BJHlvw/UzJq3I/CCci0lFAgMBAAGjITAfMB0GA1UdDgQWBBS1Lo57VqvU" +
								"BnfyJu51JO9csLJenjANBgkqhkiG9w0BAQsFAAOCAQEACcfPaVl5PIkBZ6cXyHuj" +
								"rJZkZH7Koqhx12DNeCoohdQkRda/gWeHVPsO7snK63sFhoY08OGVgvTRhgzwSBxJ" +
								"cx9GkCyojfHo5xZoOlSQ01PygyScd42DlseNiwXZGBfoxacLEYkIP6OXrDa+wNAP" +
								"gHnLI+37tzkafoPT0xoV/E9thvUUKX1jSIL5UCoGuso6FWLiZgDxD8wKgd22FcYo" +
								"T7B7DHG4R+0rgav81J9xjgOR3ayvNrb86iVvVBmrIiM7Gc2hf5PMiiAOaISST2cJ" +
								"x90X7TUA/f0qrYKveTvkRT77nLfzHV1a+PTS7PwkCXUt/NRm4VwseyGIgQ4FXH6W" +
								"zw==";
		
		
		
		
		
		//解析root CA 证书
		X509Certificate rootcaCertificate = CertificateCoder.getX509Certificate(Base64.decodeBase64(rootCerBase64));
		//解析root CA 私钥
		String rootcaDer = FileUtils.readFileToString(new File("d:\\rootcakey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = CertificateCoder.privateKeyFromPem(rootcaDer,"");
		System.out.println(rootcaPrivateKey);
		
		//1.生成用户密钥对
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();

		//2.生成用户证书请求
		PKCS10CertificationRequest p10 = genPKCS10(kp);
		SubjectPublicKeyInfo subPublicKeyInfo = p10.getSubjectPublicKeyInfo();
		RSAKeyParameters rsa = (RSAKeyParameters) PublicKeyFactory.createKey(subPublicKeyInfo);
		RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsa.getModulus(), rsa.getExponent());
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey publicKey = kf.generatePublic(rsaSpec);
		
		//3.生成用户证书
		X509Certificate clientCertificate = buildEndEntityCert(publicKey,rootcaPrivateKey,rootcaCertificate);
		FileUtils.writeByteArrayToFile(new File("d:\\client.cer"), clientCertificate.getEncoded());
		
		//4.生成用户p12文件
		storeP12(kp, new X509Certificate[]{clientCertificate,rootcaCertificate},"d:\\client.p12", "123456");
		
		
		
		
		
		FileOutputStream fos = new FileOutputStream(new File("d:\\client1.p12"));
		X509Certificate[] chain = new X509Certificate[]{
			rootcaCertificate,
			clientCertificate,
			clientCertificate
		};
		genPKCS12File(fos, kp.getPrivate(), chain );
	}
	
	
}
