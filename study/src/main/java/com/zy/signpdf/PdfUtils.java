package com.zy.signpdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.zy.security.jdk.CertificateCoder;

public class PdfUtils {

	private static final Logger logger = LoggerFactory.getLogger(PdfUtils.class);
	
	
	public static void main(String[] args) throws Exception {
		BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
		Security.addProvider(bouncyCastleProvider);
		
		KeyStore ks = CertificateCoder.getKeyStore("C:\\ssl\\openssl\\oneCA\\user.p12", CertificateCoder.KEY_STORE_P12, "user123456");
		Enumeration<String> aliaesEle = ks.aliases();
		String alia = "";
		while (aliaesEle.hasMoreElements()) {
			alia = aliaesEle.nextElement();
			logger.debug("alia: " + alia);
		}
		
		String srcFilePath = "C:\\出让人签署.pdf";
		String destFilePath = String.format("C:\\出让人签署_1.pdf");
		String keyWord = "出让人签章处";
		byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(new File("c:\\seal.bmp")));
		Certificate[] chain = CertificateCoder.getCertificateChain("C:\\ssl\\openssl\\oneCA\\user.p12", CertificateCoder.KEY_STORE_P12, alia, "user123456");;
		PrivateKey pk = CertificateCoder.getPrivateKey("C:\\ssl\\openssl\\oneCA\\user.p12", CertificateCoder.KEY_STORE_P12, alia, "user123456");;
		String digestAlgorithm = DigestAlgorithms.SHA256;
		String provider = bouncyCastleProvider.getName();
		int len = 100;
		int width = 100;
		CryptoStandard subfilter = CryptoStandard.CMS;
		String reason = "reason";
		String location = "location";
//		sign(srcFilePath, destFilePath, keyWord, imageBytes, chain, pk, digestAlgorithm, provider, len, width, subfilter, reason, location);
		sign1(srcFilePath, destFilePath, chain, pk, digestAlgorithm, provider, len, width, subfilter, reason, location);
		
	}

	public static void sign(String srcFilePath, String destFilePath, String keyWord, byte[] imageBytes, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, int len,
			int width, CryptoStandard subfilter, String reason, String location) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		P[] ps = null;
		byte[] srcBytes = null;
		try{
			fis = new FileInputStream(new File(srcFilePath));
			srcBytes = IOUtils.toByteArray(fis);
			bos = new ByteArrayOutputStream();
			ps = searchP(srcBytes, keyWord);
			
			PdfStamper pdfStamper = null;
			int index = 1;
			if (null != ps && 0 < ps.length) {
//				pdfStamper = getPdfStamper(srcBytes, bos);
//				PdfSignatureAppearance appearance = getPdfSignatureAppearance(pdfStamper, imageBytes, ps[1], len, width, reason, location);
//				makeSign(appearance, chain, pk, digestAlgorithm, provider, subfilter);
				for (int i = 0; i < ps.length; i++) {
					P p = ps[i];
//					pdfStamper = getPdfStamper(srcBytes, bos);
					if (null != p) {
						if(1 == index) {
							pdfStamper = getPdfStamper(srcBytes, bos);
						}else {
							pdfStamper = getPdfStamper(bos.toByteArray(),bos);
						}
						PdfSignatureAppearance appearance = getPdfSignatureAppearance(pdfStamper, imageBytes, p, len, width, reason, location);
						makeSign(appearance, chain, pk, digestAlgorithm, provider, subfilter);
						index++;
					}
				}
			}
			
			IOUtils.write(bos.toByteArray(), new FileOutputStream(new File(destFilePath)));
//			IOUtils.write(bos.toByteArray(), pdfStamper.getWriter().getOs());
		}catch(Exception e) {
			logger.error("签章异常【" + e + "】");
			throw new RuntimeException(e);
		}finally {
			
		}
	}
	
	private static PdfStamper getPdfStamper(byte[] srcBytes, OutputStream destOs) {
		// Creating the reader and the stamper
		PdfReader reader = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(srcBytes);
			stamper = PdfStamper.createSignature(reader, destOs, '\0');
		} catch (Exception e) {
			logger.error("获取PdfStamper异常【" + e + "】");
			throw new RuntimeException(e);
		} finally {
		}
		return stamper;
	}

	private static PdfSignatureAppearance getPdfSignatureAppearance(PdfStamper pdfStamper, byte[] imageBytes, P p, int len, int width, String reason, String location) {
		PdfSignatureAppearance pdfSignatureAppearance = null;
		try {
			// Creating the appearance
			pdfSignatureAppearance = pdfStamper.getSignatureAppearance();
			pdfSignatureAppearance.setReason(reason);
			pdfSignatureAppearance.setLocation(location);

			// 使用png格式透明图片
			Image image = Image.getInstance(imageBytes);

			// 设置签章图片 及签章的模式（图）
			pdfSignatureAppearance.setSignatureGraphic(Image.getInstance(image));
			pdfSignatureAppearance.setRenderingMode(RenderingMode.GRAPHIC);

			pdfSignatureAppearance.setVisibleSignature(new Rectangle(p.getX(), p.getY(), p.getX() + len, p.getY() + width), p.getPageNo(), "FieldName" + p.getPageNo());
		} catch (Exception e) {
			logger.error("生成PdfSignatureAppearance异常【" + e + "】");
			throw new RuntimeException(e);
		}
		return pdfSignatureAppearance;
	}

	private static void makeSign(PdfSignatureAppearance pdfSignatureAppearance, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, CryptoStandard subfilter) {
		ExternalDigest digest = new BouncyCastleDigest();
		ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, provider);
		try {
			MakeSignature.signDetached(pdfSignatureAppearance, digest, signature, chain, null, null, null, 0, subfilter);
		} catch (IOException | DocumentException | GeneralSecurityException e) {
			logger.error("签章异常【" + e + "】");
			throw new RuntimeException(e);
		}
	}

	public static void sign1(String src, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, int len, int width, CryptoStandard subfilter, String reason,
			String location) throws Exception {
		// Creating the reader and the stamper
		PdfReader reader = new PdfReader(src);

		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');

		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setReason(reason);
		appearance.setLocation(location);

		Image image = Image.getInstance("C:\\seal.bmp"); // 使用png格式透明图片

		// 设置签章图片 及签章的模式（图）
		appearance.setSignatureGraphic(image);
		appearance.setRenderingMode(RenderingMode.GRAPHIC);

		// appearance.setVisibleSignature(new Rectangle(360, 748, 460, 848), 1,
		// "保险人签章");
		appearance.setVisibleSignature(new Rectangle(69, 187, 169, 287), 1, "保险人签章");
		appearance.setVisibleSignature(new Rectangle(169, 287, 169, 287), 2, "保险人签章");

		// appearance.setVisibleSignature("保险人签章");

		// Creating the signature
		ExternalDigest digest = new BouncyCastleDigest();
		ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, provider);
		MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, subfilter);
	}

	private static P[] searchP(byte[] srcBytes, final String keyWord) {
		P[] ps = null;
		try {
			PdfReader pdfReader = new PdfReader(srcBytes);
			int pageNum = pdfReader.getNumberOfPages();
			PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);

			ps = new P[pageNum];
			// 下标从1开始
			int i = 0;
			for (i = 1; i < pageNum; i++) {

				RenderListenerImpl renderListenerImpl = new RenderListenerImpl(i, keyWord);
				pdfReaderContentParser.processContent(i, renderListenerImpl);
				ps[i - 1] = renderListenerImpl.getP();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ps;
	}

	private static class RenderListenerImpl implements RenderListener {

		private int pageNo;
		private String keyWord;
		private P p;

		public RenderListenerImpl(int pageNo, String keyWord) {
			this.pageNo = pageNo;
			this.keyWord = keyWord;
			p = new P();
		}

		@Override
		public void renderText(TextRenderInfo textRenderInfo) {
			String text = textRenderInfo.getText();
			if (null != text && text.contains(keyWord)) {
				Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
				p.setX(boundingRectange.x);
				p.setY(boundingRectange.y);
				p.setPageNo(pageNo);
			}
		}

		public P getP() {
			return p;
		}

		@Override
		public void renderImage(ImageRenderInfo arg0) {
		}

		@Override
		public void endTextBlock() {
		}

		@Override
		public void beginTextBlock() {
		}
	}

	public static class P {
		private int pageNo;
		private float x;
		private float y;

		public int getPageNo() {
			return pageNo;
		}

		public void setPageNo(int pageNo) {
			this.pageNo = pageNo;
		}

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		@Override
		public String toString() {
			return "P [pageNo=" + pageNo + ", x=" + x + ", y=" + y + "]";
		}

	}

}
