//package com.zy.ssl.httpclient;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.security.KeyStore;
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//
//
//
//import javax.net.ssl.SSLContext;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.ssl.SSLContexts;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;
//
//public class HttpClientTest {
//	
//	private static Logger logger = Logger.getLogger(HttpClientTest.class);
//
//	private static final String KEY_STORE_TYPE_JKS = "jks";
//	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
//	private static final String SCHEME_HTTPS = "https";
//	private static final int HTTPS_PORT = 8443;
//	private static final String HTTPS_URL = "https://127.0.0.1:8443/HttpClientSSL/sslServlet";
//	private static final String KEY_STORE_CLIENT_PATH = "E:/ssl/client.p12";
//	private static final String KEY_STORE_TRUST_PATH = "E:/ssl/client.truststore";
//	private static final String KEY_STORE_PASSWORD = "123456";
//	private static final String KEY_STORE_TRUST_PASSWORD = "123456";
//
//	/**
//	 * 
//	 * @Author zy
//	 * @Company: JL
//	 * @Create Time: 2014年8月13日 下午2:34:14
//	 * @param args
//	 * @throws Exception
//	 */
//	public final static void main(String[] args) throws Exception {
//		
//		
//		
//		logger.info(" =========================="+ KeyStore.getDefaultType());
//		
//		testsslSuccess();
//		
////		testSSL();
//		
////		postHttp();
//	}
//	
//	private static void testsslSuccess() throws Exception{
//		
//		String name = "client";
//		String passwd = name + "123456";
//		String url = "https://jl.ssl.com/ssl/sslListServlet";
//		
//		DefaultHttpClient httpclient = new DefaultHttpClient();
//
//        KeyStore trustStore = KeyStore.getInstance("PKCS12");   
//        FileInputStream instream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\" + name + ".p12"));
//        try {
//            trustStore.load(instream, passwd.toCharArray());
//        } finally {
//            instream.close();
//        }
//        
//        SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore,passwd,trustStore);
//        Scheme sch = new Scheme("https", socketFactory, 443);
//        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
//
//        HttpGet httpget = new HttpGet(url);
//
//        System.out.println("executing request" + httpget.getRequestLine());
//        
//        HttpResponse response = httpclient.execute(httpget);
//        HttpEntity entity = response.getEntity();
//
//        System.out.println("----------------------------------------");
//        System.out.println(response.getStatusLine());
//        if (entity != null) {
//            System.out.println("Response content length: " + entity.getContentLength());
//        }
//        if (entity != null) {
//            entity.consumeContent();
//        }
//        httpclient.getConnectionManager().shutdown();    
//
//	}
//	
//	private static void postHttp() throws Exception {
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpGet httpGet = new HttpGet("http://jl.ssl.com");
//            CloseableHttpResponse response1 = httpclient.execute(httpGet);
//            // The underlying HTTP connection is still held by the response object
//            // to allow the response content to be streamed directly from the network socket.
//            // In order to ensure correct deallocation of system resources
//            // the user MUST call CloseableHttpResponse#close() from a finally clause.
//            // Please note that if response content is not fully consumed the underlying
//            // connection cannot be safely re-used and will be shut down and discarded
//            // by the connection manager.
//            try {
//                System.out.println(response1.getStatusLine());
//                HttpEntity entity1 = response1.getEntity();
//                
//                // do something useful with the response body
//                // and ensure it is fully consumed
//                EntityUtils.consume(entity1);
//            } finally {
//                response1.close();
//            }
//
//            System.out.println("====================================================");
//            
//            
//            HttpPost httpPost = new HttpPost("https://jl.ssl.com/ssl/sslListServlet");
//            
////            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
////            nvps.add(new BasicNameValuePair("username", "vip"));
////            nvps.add(new BasicNameValuePair("password", "secret"));
////            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//            
//            CloseableHttpResponse response2 = httpclient.execute(httpPost);
//
//            try {
//                System.out.println(response2.getStatusLine());
//                HttpEntity entity2 = response2.getEntity();
//                
//                // do something useful with the response body
//                // and ensure it is fully consumed
//                EntityUtils.consume(entity2);
//            } finally {
//                response2.close();
//            }
//        }catch(Exception e) {
//        	e.printStackTrace();
//        } finally {
//            httpclient.close();
//        }
//	}
//
//
//	public static void testSSL() throws Exception {
//		
//		
//		
//		KeyStore keyStore  = KeyStore.getInstance(KEY_STORE_TYPE_P12);  
//        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());  
//        
//        InputStream ksIn = new FileInputStream("C:\\Users\\Administrator\\Desktop\\client.p12");  
//        InputStream tsIn = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\client.keystore"));  
//        try {  
//            keyStore.load(ksIn, "client123456".toCharArray());  
//            trustStore.load(tsIn, "client123456".toCharArray());  
//        }catch(Exception e ) {
//			logger.error("11111111============" +e);
//		} finally {  
//            try { ksIn.close(); } catch (Exception ignore) {}  
//            try { tsIn.close(); } catch (Exception ignore) {}  
//        }  
//
//		// Trust own CA and all self-signed certs
//		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
//				.build();
//
//		// Allow TLSv1 protocol only
//		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
//				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//		try {
//
//			HttpGet httpget = new HttpGet("https://jl.ssl.com/ssl/sslListServlet");
//
//			System.out.println("executing request" + httpget.getRequestLine());
//
//			CloseableHttpResponse response = httpclient.execute(httpget);
//			try {
//				HttpEntity entity = response.getEntity();
//
//				System.out.println("----------------------------------------");
//				System.out.println(response.getStatusLine());
//				if (entity != null) {
//					System.out.println("Response content length: " + entity.getContentLength());
//				}
//				EntityUtils.consume(entity);
//			}catch (Exception e ) {
//				logger.error("22222==========" + e );
//			} finally {
//				response.close();
//			}
//		} catch (Exception e ) {
//			logger.error("33333==========" +e);
//		}finally {
//			httpclient.close();
//		}
//	}
//
//}
