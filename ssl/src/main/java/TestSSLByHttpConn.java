import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;



public class TestSSLByHttpConn {
	
	private static final String KEY_STORE_TYPE_JKS = "jks";
	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	
	public static void main(String[] args) throws Exception{		
		
		URL url = new URL("https://jl.ssl.com:8443/study/dynamicServlet");
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		
		configurationSSLSOcketFactory(connection,"c:\\ssl\\client.p12","client123456","c:\\ssl\\server.truststore","client123456");
		
		InputStream in = connection.getInputStream();
		
		in.close();
		
	}

	/**
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月21日 下午4:04:54
	 * @param connection
	 * @param keyStorePath
	 * @param keyStorePassword
	 * @param trustKeyStorePath
	 * @param trustKeySotrePassword
	 * @throws Exception
	 */
	private static void configurationSSLSOcketFactory(HttpsURLConnection connection, String keyStorePath, String keyStorePassword, String trustKeyStorePath, String trustKeySotrePassword) throws Exception{
		SSLSocketFactory sf = getSSLSocketFactory(keyStorePath, keyStorePassword, trustKeyStorePath, trustKeySotrePassword);
		connection.setSSLSocketFactory(sf);
		
	}

	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月21日 下午4:05:05
	 * @param keyStorePath
	 * @param keyStorePassword
	 * @param trustKeyStorePath
	 * @param trustKeySotrePassword
	 * @return
	 * @throws Exception
	 */
	private static SSLSocketFactory getSSLSocketFactory(String keyStorePath,String keyStorePassword,String trustKeyStorePath,String trustKeySotrePassword) throws Exception {
		
		//初始化密钥库
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		KeyStore keyStore = getKeyStore(keyStorePath,KEY_STORE_TYPE_P12, keyStorePassword);
		
		//初始化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
		KeyStore trusKeyStore = getKeyStore(trustKeyStorePath,KEY_STORE_TYPE_JKS, trustKeySotrePassword);
		
		keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
		trustManagerFactory.init(trusKeyStore);
		
		//初始化SSL上下文
		SSLContext ctx = SSLContext.getInstance("SSL");
		
		ctx.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),null);
		
		SSLSocketFactory sf = ctx.getSocketFactory();
		
		return sf;
		
	}
	
	/**
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月21日 下午4:05:01
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(String keyStorePath,String type,String password) throws Exception{
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(type);
		ks.load(is,password.toCharArray());
		is.close();
		return ks;
	}
	
}












































