package com.zy.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

	private SSLSocketFactory socketFactory;

	public AbstractHttps(InputStream keyStoreInputStream, String keyStorePassword, InputStream trustKeyStoreInputStream, String trustKeySotrePassword) {
		socketFactory = initSSLSocketFactory(keyStoreInputStream, keyStorePassword, trustKeyStoreInputStream, trustKeySotrePassword);
	}

	public AbstractHttps(String keyStorePath, String keyStorePassword, String trustKeyStorePath, String trustKeySotrePassword) {
		socketFactory = initSSLSocketFactory(keyStorePath, keyStorePassword, trustKeyStorePath, trustKeySotrePassword);
	}

	protected HttpsURLConnection getConnection(String urlAddress) throws Exception {
		URL url = new URL(urlAddress);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setHostnameVerifier(NoopHostnameVerifier.INSTANCE);
		configurationSSLSOcketFactory(connection);
		return connection;
	}

	private void configurationSSLSOcketFactory(HttpsURLConnection connection) throws Exception {
		connection.setSSLSocketFactory(socketFactory);
	}
	
	private SSLSocketFactory initSSLSocketFactory(String keyStorePath, String keyStorePassword, String trustKeyStorePath, String trustKeySotrePassword) throws RuntimeException {
		SSLSocketFactory sf = null;
		InputStream keyStoreInputStream = null;
		InputStream trustKeyStoreInputStream = null;
		try{
			keyStoreInputStream = new FileInputStream(keyStorePath);
			trustKeyStoreInputStream = new FileInputStream(trustKeyStorePath);
			sf = initSSLSocketFactory(keyStoreInputStream, keyStorePassword, trustKeyStoreInputStream, trustKeySotrePassword);
		}catch(Exception e) {
			throw new RuntimeException("初始化SSLSocketFactory异常【" + e + "】");
		}finally {
			if(null != trustKeyStoreInputStream) {
				try {
					trustKeyStoreInputStream.close();
				} catch (IOException e) {
					trustKeyStoreInputStream = null;
					throw new RuntimeException("关闭trustKeyStoreInputStream异常【" + e + "】");
				}
			}
			if(null != keyStoreInputStream) {
				try {
					keyStoreInputStream.close();
				} catch (IOException e) {
					keyStoreInputStream = null;
					throw new RuntimeException("关闭keyStoreInputStream异常【" + e + "】");
				}
			}
			
		}
		return sf;
	}

	private SSLSocketFactory initSSLSocketFactory(InputStream keyStoreInputStream, String keyStorePassword, InputStream trustKeyStoreInputStream, String trustKeySotrePassword) throws RuntimeException {
		SSLSocketFactory sf = null;
		try{
			// 初始化密钥库
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(C.Certificate.ALGORITHM_SUNX509);
			KeyStore keyStore = getKeyStore(keyStoreInputStream, C.Certificate.KEY_STORE_TYPE_P12, keyStorePassword);
	
			// 初始化信任库
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(C.Certificate.ALGORITHM_SUNX509);
			KeyStore trusKeyStore = getKeyStore(trustKeyStoreInputStream, C.Certificate.KEY_STORE_TYPE_JKS, trustKeySotrePassword);
	
			keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
			trustManagerFactory.init(trusKeyStore);
	
			// 初始化SSL上下文
			SSLContext ctx = SSLContext.getInstance(C.Certificate.PROTOCOL_SSL);
	
			ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
	
			sf = ctx.getSocketFactory();
		}catch(Exception e) {
			throw new RuntimeException("初始化SSLSocketFactory异常【" + e + "】");
		}finally {
		}
		return sf;

	}

	@SuppressWarnings("unused")
	private KeyStore getKeyStore(String keyStorePath, String type, String password) throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(type);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}
	
	private KeyStore getKeyStore(InputStream is, String type, String password) throws Exception {
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
