package com.zy.ssl;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Enumeration;


/**
 * 
 * @Project: resolveexcel
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年8月12日 上午8:59:25
 */
public class ReadP12Cert {

	/**
	 * TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// final String KEYSTORE_FILE = "c:\\tomcat.keystore";
		final String KEYSTORE_FILE = "d:\\client.p12";

		final String KEYSTORE_PASSWORD = "jl123456";

//		final String KEYSTORE_ALIAS = "alias";

		try {

			KeyStore ks = KeyStore.getInstance("PKCS12");
			// KeyStore ks = KeyStore.getInstance("JKS");

			FileInputStream fis = new FileInputStream(KEYSTORE_FILE);
			// If the keystore password is empty(""), then we have to set
			// to null, otherwise it won't work!!!
			char[] nPassword = null;
			if ((KEYSTORE_PASSWORD == null) || KEYSTORE_PASSWORD.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = KEYSTORE_PASSWORD.toCharArray();
			}
			ks.load(fis, nPassword);
			fis.close();
			System.out.println("keystore type=" + ks.getType());
			// Now we loop all the aliases, we need the alias to get keys.

			// It seems that this value is the "Friendly name" field in the

			// detals tab <-- Certificate window <-- view <-- Certificate

			// Button <-- Content tab <-- Internet Options <-- Tools menu

			// In MS IE 6.
			@SuppressWarnings("rawtypes")
			Enumeration enum1 = ks.aliases();
			String keyAlias = null;
			if (enum1.hasMoreElements()) {// we are readin just one certificate.
				keyAlias = (String) enum1.nextElement();
				System.out.println("alias=[" + keyAlias + "]");
			}
			// Now once we know the alias, we could get the keys.
			System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
			
			PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			Certificate cert = ks.getCertificate(keyAlias);
			PublicKey pubkey = cert.getPublicKey();
			
			System.out.println("cert class = " + cert.getClass().getName());
			System.out.println("cert = " + cert);
			System.out.println("public key = " + pubkey);
			System.out.println("private key = " + prikey);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
