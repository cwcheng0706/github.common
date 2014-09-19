package com.jl.ssl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class CopyOfHttpClientUtils {

	private static final String KEY_STORE_TYPE_JKS = "jks";
	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	private static final String SCHEME_HTTPS = "https";
	
	private static final int HTTPS_PORT = 8443;
	private static final String HTTPS_URL = "https://jl.ssl.com/service/post";
	private static final String KEY_STORE_CLIENT_PATH = "C:\\Users\\Administrator\\Desktop\\client.p12";
	private static final String KEY_STORE_TRUST_PATH = "C:\\Users\\Administrator\\Desktop\\client.truststore";
	private static final String KEY_STORE_PASSWORD = "client123456";
	private static final String KEY_STORE_TRUST_PASSWORD = "server123456";


	public static void main(String[] args)  throws Exception{
		
		CopyOfHttpClientUtils.ssl();
		
		
	}
	
	
	/**
	 * 发送https请求 为双向请求
	 * 
	 * @param url
	 * @param xmlStr
	 * @return
	 */
	public static String ssl() throws Exception {
		String ret = "";
		HttpClient httpClient = new DefaultHttpClient();
		try {
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
			KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_JKS);

			InputStream ksIn = new FileInputStream(KEY_STORE_CLIENT_PATH);
			InputStream tsIn = new FileInputStream(new File(KEY_STORE_TRUST_PATH));

			try {
				keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
				trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
			} finally {
				try {
					ksIn.close();
				} catch (Exception ignore) {
				}
				try {
					tsIn.close();
				} catch (Exception ignore) {
				}
			}
			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore, KEY_STORE_PASSWORD, trustStore);

			Scheme sch = new Scheme(SCHEME_HTTPS, HTTPS_PORT, socketFactory);
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);

			HttpPost post = new HttpPost(HTTPS_URL);

			StringEntity reqEntity = new StringEntity("<html>你好啊啊</html>", "UTF-8");

			post.setEntity(reqEntity);

			System.out.println("executing request" + post.getRequestLine());
			HttpResponse response = httpClient.execute(post);
			HttpEntity respEntity = response.getEntity();

			System.out.println(response.getStatusLine());

			StringBuilder sb = new StringBuilder();
			if (respEntity != null) {
				System.out.println("Response content length: " + respEntity.getContentLength());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(respEntity.getContent(),"UTF-8"));
				String text;

				while ((text = bufferedReader.readLine()) != null) {
					sb.append(text);
				}
				bufferedReader.close();

			}
//			System.out.println("----------------------------------------");
//			System.out.println(sb.toString());
			ret = sb.toString();
//			System.out.println("----------------------------------------");
			EntityUtils.consume(respEntity);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return ret;
	}


}
