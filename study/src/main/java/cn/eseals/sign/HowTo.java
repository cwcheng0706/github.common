package cn.eseals.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class HowTo {
	
	// 证书密码
	private static final String cert_pwd = "1";

	private static final String cert_path = "F:\\machunlin_1.pfx";
	
	public static void main(String[] args) {
		HowTo h = new HowTo();
		try {
			h.cratePdf();
			h.sign();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}  
		System.out.println("done!!!");
		//h.md4();
		//h.testBC();
	}
	
	

	public static void sign() throws Exception{
		// Security.addProvider(new
		// org.bouncycastle.jce.provider.BouncyCastleProvider());

		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(new FileInputStream(cert_path), cert_pwd.toCharArray());
		String alias = (String)ks.aliases().nextElement();
		PrivateKey key = (PrivateKey)ks.getKey(alias, cert_pwd.toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);
		PdfReader reader = new PdfReader("F:\\feiqing.pdf");
		FileOutputStream fout = new FileOutputStream("F:\\signed.pdf");
		PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
		
		
		PdfSignatureAppearance sap = stp.getSignatureAppearance();
//		sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
		
		sap.setReason("I'm the author");
		sap.setLocation("Ma");
		// comment next line to have an invisible signature
		sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
				
		stp.close();
	}

	

	public static void md4() {
		Security
				.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD4");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update("test".getBytes());
		System.out.println("md4:" + md.digest());
	}

	public static void testBC() {
		Provider provider = Security.getProvider("BC");
		System.err.print(provider);
		for(Map.Entry<Object, Object> entry: provider.entrySet()){
			System.out.println(entry.getKey() + "  ------  " + entry.getValue());
		}
	}
	
	public static void cratePdf() throws FileNotFoundException, DocumentException{
		Document doc = new Document();
		PdfWriter.getInstance(doc, new FileOutputStream("F:\\newDoc.pdf"));
		doc.open();
		doc.add(new Paragraph("hello world"));
		doc.close();
	}
}
