package com.zy.signpdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.zy.security.jdk.CertificateCoder;

public class SignPdf {

	public static void main(String[] args) throws Exception {
		KeyStore ks = CertificateCoder.getKeyStore("C:\\ssl\\openssl\\oneCA\\user.p12", CertificateCoder.KEY_STORE_P12, "user123456");
		Enumeration<String> aliaesEle = ks.aliases();
		String ele = "";
		while (aliaesEle.hasMoreElements()) {
			ele = aliaesEle.nextElement();
			System.out.println("alia: " + ele);
		}

		Certificate[] chain = CertificateCoder.getCertificateChain("C:\\ssl\\openssl\\oneCA\\user.p12", CertificateCoder.KEY_STORE_P12, ele, "user123456");
		System.out.println(chain.length);

		PrivateKey pk = CertificateCoder.getPrivateKey("C:\\ssl\\openssl\\oneCA\\user.p12", CertificateCoder.KEY_STORE_P12, ele, "user123456");

		BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
//		String SRC = "C:\\testNo5.pdf";
		
//		float[] position = PdfUtils.getKeyWords("C:\\出让人签署.pdf");
		
		
		
		// String DEST = "C:\\testNo5_1.pdf";

		sign("C:\\testNo5.pdf", String.format("C:\\testNo5_1.pdf", 1), chain, pk, DigestAlgorithms.SHA256, provider.getName(), CryptoStandard.CMS, "保险人签章", "保险人签章");
		sign("C:\\出让人签署.pdf", String.format("C:\\出让人签署_1.pdf", 1), chain, pk, DigestAlgorithms.SHA256, provider.getName(), CryptoStandard.CMS, "保险人签章", "保险人签章");
//		sign(SRC, String.format("C:\\testNo5_2.pdf", 2), chain, pk, DigestAlgorithms.SHA512, provider.getName(), CryptoStandard.CMS, "保险人签章", "保险人签章");
//		sign(SRC, String.format("C:\\testNo5_3.pdf", 3), chain, pk, DigestAlgorithms.SHA256, provider.getName(), CryptoStandard.CADES, "保险人签章", "保险人签章");
//		sign(SRC, String.format("C:\\testNo5_4.pdf", 4), chain, pk, DigestAlgorithms.RIPEMD160, provider.getName(), CryptoStandard.CADES, "保险人签章", "保险人签章");

	}

	public static void sign(String src, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, CryptoStandard subfilter, String reason, String location)
			throws GeneralSecurityException, IOException, DocumentException {
		// Creating the reader and the stamper
		PdfReader reader = new PdfReader(src);

		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');

		// 预留手动签章证书的地方
		// PdfFormField field =
		// PdfFormField.createSignature(stamper.getWriter());
		// field.setFieldName("保险人签章");
		// // set the widget properties
		// field.setWidget(new Rectangle(72, 732, 144, 780),
		// PdfAnnotation.HIGHLIGHT_OUTLINE);
		// field.setFlags(PdfAnnotation.FLAGS_PRINT);
		// // add the annotation
		// stamper.addAnnotation(field, 1);

		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setReason(reason);
		appearance.setLocation(location);

		Image image = Image.getInstance("C:\\seal.bmp"); // 使用png格式透明图片
		// appearance.setImage(image);

		// 设置签章图片 及签章的模式（图）
		appearance.setSignatureGraphic(image);
		appearance.setRenderingMode(RenderingMode.GRAPHIC);

//		appearance.setVisibleSignature(new Rectangle(360, 748, 460, 848), 1, "保险人签章");
		appearance.setVisibleSignature(new Rectangle(69, 187, 169, 287), 1, "保险人签章");
		
		// appearance.setVisibleSignature("保险人签章");

		// Creating the signature
		ExternalDigest digest = new BouncyCastleDigest();
		ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, provider);
		MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, subfilter);
	}

}
