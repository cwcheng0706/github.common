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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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

		String p12Path = "D:\\ssl\\2.199\\openssl\\oneCA\\user.p12";
		String p12Pass = "user123456";
		String sealBmpPath = "c:\\seal.bmp";

		String srcFilePath = "C:\\出让人签署.pdf";
		String destFilePath = String.format("C:\\出让人签署1.pdf");
		String[] keyWords = new String[]{"客户签章处","见证方签章处","理财师签章处"};
//		keyWords = new String[] {"见证方签章处"};

		KeyStore ks = CertificateCoder.getKeyStore(p12Path, CertificateCoder.KEY_STORE_P12, p12Pass);
		Enumeration<String> aliaesEle = ks.aliases();
		String alia = "";
		while (aliaesEle.hasMoreElements()) {
			alia = aliaesEle.nextElement();
			logger.debug("alia: " + alia);
		}

		byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(new File(sealBmpPath)));
		Certificate[] chain = CertificateCoder.getCertificateChain(p12Path, CertificateCoder.KEY_STORE_P12, alia, p12Pass);
		PrivateKey pk = CertificateCoder.getPrivateKey(p12Path, CertificateCoder.KEY_STORE_P12, alia, p12Pass);
		String digestAlgorithm = DigestAlgorithms.SHA256;
		String provider = bouncyCastleProvider.getName();
		int len = 50;
		int width = 50;
		CryptoStandard subfilter = CryptoStandard.CMS;
		String reason = "reason";
		String location = "location";
		
//		sign(srcFilePath, destFilePath, imageBytes, chain, pk, digestAlgorithm, provider, len, width, subfilter, reason, location, keyWords);
		
		byte[] signBytes = sign(IOUtils.toByteArray(new FileInputStream(new File(srcFilePath))), imageBytes, chain, pk, digestAlgorithm, provider, len, width, subfilter, reason, location, keyWords);
		IOUtils.write(signBytes, new FileOutputStream(new File(destFilePath)));
	}

	public static void sign(String srcFilePath, String destFilePath, byte[] imageBytes, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, int len,
			int width, CryptoStandard subfilter, String reason, String location, String... keyWords) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		List<P> pList = null;
		byte[] srcBytes = null;
		try {
			fis = new FileInputStream(new File(srcFilePath));
			srcBytes = IOUtils.toByteArray(fis);
			bos = new ByteArrayOutputStream();
			pList = searchP(srcBytes, keyWords);

			PdfStamper pdfStamper = null;
			if (null != pList && 0 < pList.size()) {
				for (int i = 0; i < pList.size(); i++) {
					P p = pList.get(i);
					if (null != p) {
						pdfStamper = getPdfStamper(srcBytes, bos);
						PdfSignatureAppearance appearance = getPdfSignatureAppearance(pdfStamper, imageBytes, p, len, width, reason, location);
						makeSign(appearance, chain, pk, digestAlgorithm, provider, subfilter);
						
//						File temp = new File("c:\\temp_" + index + ".pdf");
//						IOUtils.write(bos.toByteArray(), new FileOutputStream(temp));
//						srcBytes = IOUtils.toByteArray(new FileInputStream(temp));
						srcBytes = bos.toByteArray();
						if(i < (pList.size() -1)) {
							bos.reset();
						}
					}
				}
			}

			IOUtils.write(bos.toByteArray(), new FileOutputStream(new File(destFilePath)));
		} catch (Exception e) {
			logger.error("签章异常【" + e + "】");
			throw new RuntimeException(e);
		} finally {
			if(null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					bos = null;
					logger.error("关闭输出字节流异常【" + e + "】");
					throw new RuntimeException(e);
				}
			}
			if(null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					fis = null;
					logger.error("关闭读源文件流异常【" + e + "】");
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static byte[] sign(byte[] srcFileBytes, byte[] imageBytes, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, int len,
			int width, CryptoStandard subfilter, String reason, String location, String... keyWords) {
		ByteArrayOutputStream bos = null;
		List<P> pList = null;
		byte[] srcBytes = srcFileBytes;
		try {
			bos = new ByteArrayOutputStream();
			
			
			pList = searchP(srcBytes, keyWords);

			PdfStamper pdfStamper = null;
			if (null != pList && 0 < pList.size()) {
				for (int i = 0; i < pList.size(); i++) {
					P p = pList.get(i);
					if (null != p) {
						pdfStamper = getPdfStamper(srcBytes, bos);
						PdfSignatureAppearance appearance = getPdfSignatureAppearance(pdfStamper, imageBytes, p, len, width, reason, location);
						makeSign(appearance, chain, pk, digestAlgorithm, provider, subfilter);
						
//						File temp = new File("c:\\temp_" + index + ".pdf");
//						IOUtils.write(bos.toByteArray(), new FileOutputStream(temp));
//						srcBytes = IOUtils.toByteArray(new FileInputStream(temp));
						srcBytes = bos.toByteArray();
						if(i < pList.size() -1) {
							bos.reset();
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("签章异常【" + e + "】");
			throw new RuntimeException(e);
		} finally {
			if(null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					bos = null;
					logger.error("关闭输出字节流异常【" + e + "】");
					throw new RuntimeException(e);
				}
			}
		}
		return bos.toByteArray();
	}

	private static PdfStamper getPdfStamper(byte[] srcBytes, OutputStream destOs) {
		// Creating the reader and the stamper
		PdfReader reader = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(srcBytes);
//			PdfReader.unethicalreading = true;
			stamper = PdfStamper.createSignature(reader, destOs, '\0', null, true);
//			stamper = PdfStamper.createSignature(reader, destOs, '\0');
			// 11是阅读、编辑的密码，22是阅读的密码
			// stamper.setEncryption("11".getBytes(),"22".getBytes(),PdfWriter.ALLOW_SCREENREADERS,PdfWriter.STANDARD_ENCRYPTION_128);

		} catch (Exception e) {
			logger.error("获取PdfStamper异常【" + e + "】");
			throw new RuntimeException(e);
		} finally {
		}
		return stamper;
	}
	
	@SuppressWarnings("unused")
	private static PdfStamper getPdfStamper(PdfReader pdfReader, OutputStream destOs) {
		// Creating the reader and the stamper
		PdfReader reader = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(pdfReader);
			// PdfReader.unethicalreading = true;
			stamper = PdfStamper.createSignature(reader, destOs, '\0', null, true);
//			stamper = PdfStamper.createSignature(reader, destOs, '\0');
			// 11是阅读、编辑的密码，22是阅读的密码
			// stamper.setEncryption("11".getBytes(),"22".getBytes(),PdfWriter.ALLOW_SCREENREADERS,PdfWriter.STANDARD_ENCRYPTION_128);

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

			pdfSignatureAppearance.setVisibleSignature(new Rectangle(p.getX(), p.getY(), p.getX() + len, p.getY() + width), p.getPageNo(), "" + p.getPageNo() + System.nanoTime());
//			pdfSignatureAppearance.setVisibleSignature(new Rectangle(p.getX(), p.getY(), p.getX() + len, p.getY() + width), 3, "" + p.getPageNo() + System.nanoTime());
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

	private static List<P> searchP(byte[] srcBytes, String... keyWords) {
		List<P> pList = null;
		try {
			PdfReader pdfReader = new PdfReader(srcBytes);
			int pageNum = pdfReader.getNumberOfPages();
			PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);

			pList = new ArrayList<PdfUtils.P>(pageNum);
			// 下标从1开始
			int i = 0;
			for (i = 1; i <= pageNum; i++) {
				RenderListenerImpl renderListenerImpl = new RenderListenerImpl(i, keyWords);
				pdfReaderContentParser.processContent(i, renderListenerImpl);
				if (null != renderListenerImpl.getPs() && 0 < renderListenerImpl.getPs().size()) {
					pList.addAll(renderListenerImpl.getPs());
				}
			}
		} catch (IOException e) {
			logger.error("搜索关键字异常【" + e + "】");
			throw new RuntimeException(e);
		}
		return pList;
	}

	private static class RenderListenerImpl implements RenderListener {

		private int pageNo;
		private String[] keyWordArr;
		private List<P> pList;

		public RenderListenerImpl(int pageNo, String[] keyWordArr) {
			this.pageNo = pageNo;
			this.keyWordArr = keyWordArr;
			pList = new ArrayList<P>();
		}

		@Override
		public void renderText(TextRenderInfo textRenderInfo) {
			String text = textRenderInfo.getText();
			if (null != text ){
				if(null != keyWordArr && 0 < keyWordArr.length) {
					for(int i = 0 ; i < keyWordArr.length ; i++) {
						String _keyWord = keyWordArr[i];
						if(text.contains(_keyWord)) {
							P p = new P();
							Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
							p.setX(boundingRectange.x);
							p.setY(boundingRectange.y);
							p.setPageNo(pageNo);
							pList.add(p);
						}
					}
				}
			}
		}

		public List<P> getPs() {
			return pList;
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
