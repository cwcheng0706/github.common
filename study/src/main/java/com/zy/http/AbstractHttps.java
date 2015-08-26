package com.zy.http;

import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public abstract class AbstractHttps {

	private String keyStorePath;
	private String keyStorePassword;
	private String trustKeyStorePath;
	private String trustKeySotrePassword;
	private SSLSocketFactory socketFactory;

	public AbstractHttps(String keyStorePath, String keyStorePassword, String trustKeyStorePath, String trustKeySotrePassword) {
		try {
			init(keyStorePath, keyStorePassword, trustKeyStorePath, trustKeySotrePassword);
			socketFactory = initSSLSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException("初始化SSL异常【" + e + "】");
		}
	}

	protected HttpsURLConnection getConnection(String urlAddress) throws Exception {
		URL url = new URL(urlAddress);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setHostnameVerifier(NoopHostnameVerifier.INSTANCE);
		configurationSSLSOcketFactory(connection);
		return connection;
	}

	public void init(String keyStorePath, String keyStorePassword, String trustKeyStorePath, String trustKeySotrePassword) {
		this.keyStorePath = keyStorePath;
		this.keyStorePassword = keyStorePassword;
		this.trustKeyStorePath = trustKeyStorePath;
		this.trustKeySotrePassword = trustKeySotrePassword;
	}

	private void configurationSSLSOcketFactory(HttpsURLConnection connection) throws Exception {

		connection.setSSLSocketFactory(socketFactory);
	}

	private SSLSocketFactory initSSLSocketFactory() throws Exception {

		// 初始化密钥库
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(C.Certificate.ALGORITHM_SUNX509);
		KeyStore keyStore = getKeyStore(keyStorePath, C.Certificate.KEY_STORE_TYPE_P12, keyStorePassword);

		// 初始化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(C.Certificate.ALGORITHM_SUNX509);
		KeyStore trusKeyStore = getKeyStore(trustKeyStorePath, C.Certificate.KEY_STORE_TYPE_JKS, trustKeySotrePassword);

		keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
		trustManagerFactory.init(trusKeyStore);

		// 初始化SSL上下文
		SSLContext ctx = SSLContext.getInstance(C.Certificate.PROTOCOL_SSL);

		ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

		SSLSocketFactory sf = ctx.getSocketFactory();

		return sf;

	}

	private KeyStore getKeyStore(String keyStorePath, String type, String password) throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(type);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	protected static class NoopHostnameVerifier implements HostnameVerifier {

		public static final NoopHostnameVerifier INSTANCE = new NoopHostnameVerifier();

		@Override
		public final String toString() {
			return "NO_OP";
		}

		@Override
		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}

	}
}
