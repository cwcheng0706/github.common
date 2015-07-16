package com.zy.security;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import com.zy.security.jdk.CertificateCoder;

public class PKCSCertificateGenerator {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static void main(String[] args) throws Exception {
		
		//解析root CA 证书
		String rootcaCer = FileUtils.readFileToString(new File("d:\\rootcacer.pem"), "UTF-8");
		X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
		//解析root CA 私钥
		String rootcaKey = FileUtils.readFileToString(new File("d:\\rootcakey.pem"), "UTF-8");
		PrivateKey rootcaPrivateKey = CertificateCoder.privateKeyFromPem(rootcaKey,"");
		
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
		
		
	}

	/**
	 * 证书请求
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2015年7月16日 下午2:49:39
	 * @param kp
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * Build a sample V3 certificate to use as an end entity certificate
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
				new X500Principal("CN=zhuyong,OU=JL,O=JL Corporation,L=SH_L,ST=SH,C=CN"), 
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
}
