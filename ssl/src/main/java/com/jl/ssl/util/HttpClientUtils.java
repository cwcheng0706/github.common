package com.jl.ssl.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	private static Log log = LogFactory.getLog(HttpClientUtils.class);
	private static final String KEY_STORE_TYPE_JKS = "jks";
	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	private static final String SCHEME_HTTPS = "https";
	private static final int HTTPS_PORT = 8443;
	private static final String HTTPS_URL = "https://open.jlfex.com/atlantis/rest?method=jl.order.get&timestamp=2014-08-25%2B16:25:03&format=json&v=1.0&orderCodes=2014010000139&serial_no=9900002100000000011";
	private static final String KEY_STORE_CLIENT_PATH = "C:\\https\\401.p12";
	private static final String KEY_STORE_TRUST_PATH = "C:\\https\\server.truststore";
	private static final String KEY_STORE_PASSWORD = "bz65ne0wlfv0";
	private static final String KEY_STORE_TRUST_PASSWORD = "IshmwQT7";

	private static final String CONTENT_TYPE_PDF = "application/pdf";
	private static final String CONTENT_TYPE_JSON = "application/json";

	private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>();

	static {
		CONTENT_TYPE_MAP.put(CONTENT_TYPE_PDF, ".pdf");
	}

	public static void main(String[] args) throws Exception {
		ssl();

		// FileInputStream ksIn = new FileInputStream(new
		// File("C:\\Users\\Administrator\\Desktop\\client.p12"));
		// FileInputStream tsIn = new FileInputStream(new
		// File("C:\\Users\\Administrator\\Desktop\\client.truststore"));
		//
		// HttpClientUtils.ssl(ksIn, "client123456", tsIn,
		// "server123456","https://jl.ssl.com:8443/service/post");

	}

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
			// SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore,
			// KEY_STORE_PASSWORD);

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
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(respEntity.getContent()));
				String text;

				while ((text = bufferedReader.readLine()) != null) {
					sb.append(text);
				}
				bufferedReader.close();

			}
			System.out.println("----------------------------------------");
			System.out.println(sb.toString());
			ret = sb.toString();
			System.out.println("----------------------------------------");
			EntityUtils.consume(respEntity);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return ret;
	}

	/**
	 * 发送https请求 为双向请求
	 * 
	 * @param url
	 * @param xmlStr
	 * @return
	 */
	public static String ssl(InputStream ksIn, String ksPasswd, InputStream tsIn, String tsPasswd, String url,
			String method,int port) throws Exception {
		String ret = "";
		HttpClient httpClient = new DefaultHttpClient();
		try {
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
			KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_JKS);

			// InputStream ksIn = new FileInputStream(KEY_STORE_CLIENT_PATH);
			// InputStream tsIn = new FileInputStream(new
			// File(KEY_STORE_TRUST_PATH));

			try {
				keyStore.load(ksIn, ksPasswd.toCharArray());
				trustStore.load(tsIn, tsPasswd.toCharArray());
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
			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore, ksPasswd, trustStore);
			// SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore,
			// KEY_STORE_PASSWORD);

			Scheme sch = new Scheme(SCHEME_HTTPS, port, socketFactory);
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);

			HttpResponse response = null;

			StringEntity reqEntity = new StringEntity("<html>你好啊啊</html>", "UTF-8");
			if (null != method && "post".equals(method)) {
				HttpPost post = new HttpPost(url);
				post.setEntity(reqEntity);
				System.out.println("executing request" + post.getRequestLine());
				response = httpClient.execute(post);
			} else {
				HttpGet get = new HttpGet(url);
				response = httpClient.execute(get);

			}

			HttpEntity respEntity = response.getEntity();

			System.out.println(response.getStatusLine());

			StringBuilder sb = null;
			BufferedReader bufferedReader = null;
			if (respEntity != null) {

				/**
				 * application/pdf;charset=UTF-8 application/json
				 */
				String contentTypee = getContentType(response);
				if (-1 != contentTypee.indexOf(CONTENT_TYPE_PDF)) {
					InputStream is = respEntity.getContent();
					OutputStream os = null;
					BufferedOutputStream bos = null;
					BufferedInputStream bis = null;
					try {
						File file = new File("respons" + CONTENT_TYPE_MAP.get(CONTENT_TYPE_PDF));
						bis = new BufferedInputStream(is);
						os = new FileOutputStream(file);
						bos = new BufferedOutputStream(os);
						
						byte[] buffer = new byte[2048];
						int len = 0;
						while(-1 != (len = bis.read(buffer))) {
							bos.write(buffer,0,len);
						}
						bos.flush();
						
						ret = "返回生成的文件地址【" + file.getAbsolutePath() +"】";
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						if(null != bos) {
							bos.close();
						}
						if(null != os) {
							os.close();
						}
						if(null != bis) {
							bis.close();
						}
						if(null != is) {
							is.close();
						}
					}

				} else if (-1 != contentTypee.indexOf(CONTENT_TYPE_JSON)) {
					bufferedReader = new BufferedReader(new InputStreamReader(respEntity.getContent(), "UTF-8"));
					sb = new StringBuilder();
					System.out.println("Response content length: " + respEntity.getContentLength());
					
					String text;

					while ((text = bufferedReader.readLine()) != null) {
						sb.append(text);
					}
					bufferedReader.close();
					
					ret = sb.toString();
				}

			}
			// System.out.println("----------------------------------------");
			// System.out.println(sb.toString());
			
			// System.out.println("----------------------------------------");
			EntityUtils.consume(respEntity);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return ret;
	}

	private static String getContentType(HttpResponse response) {
		String contentType = "";
		Header[] header = response.getHeaders("Content-Type");
		if (null != header) {
			for (int i = 0; i < header.length; i++) {
				Header h = header[i];
				String name = h.getName();
				String value = h.getValue();
				if ("Content-Type".equals(name)) {
					contentType = value;
				}
			}
		}

		return contentType;
	}

	/**
	 * 使用httpClient 发出http请求 均为POST请求
	 * 
	 * @param url
	 * @param xmlStr
	 * @return
	 */
	public static String httpClient(String url, String xmlStr) {
		String returnStr = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("content-type", "text/xml; charset=UTF-8");
		StringEntity strEntity;
		try {
			strEntity = new StringEntity(xmlStr, "UTF-8");
			post.setEntity(strEntity);
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
				StringBuffer buff = new StringBuffer();
				while ((returnStr = reader.readLine()) != null) {
					buff.append(returnStr);
				}
				returnStr = buff.toString();
				// returnStr=EntityUtils.toString(entity);
				log.info("returnStr:" + returnStr);
				post.abort();

				if (response != null) {
					Header headers[] = response.getAllHeaders();
					int i = 0;
					while (i < headers.length) {
						log.debug(headers[i].getName() + ":  " + headers[i].getValue());
						i++;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.info("程序出错：" + e.getMessage());
		} catch (ClientProtocolException e) {
			log.info("程序出错：" + e.getMessage());
		} catch (IOException e) {
			log.info("程序出错：" + e.getMessage());
		} finally {
			client.getConnectionManager().shutdown();
		}

		return returnStr;
	}

	/**
	 * 发送https请求 为单向请求
	 * 
	 * @param url
	 * @param xmlStr
	 * @return
	 */
	public static String httpsClient(String url, String xmlStr) {
		long responseLength = 0; // 响应长度
		String responseContent = ""; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null; // return new
								// java.security.cert.X509Certificate[0];
			}
		};
		try {
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");

			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);

			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

			HttpPost httpPost = new HttpPost(url); // 创建HttpPost
			// List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// //构建POST请求的表单参数
			// for(Map.Entry<String,String> entry : params.entrySet()){
			// formParams.add(new BasicNameValuePair(entry.getKey(),
			// entry.getValue()));
			// }
			httpPost.setEntity(new StringEntity(xmlStr, "UTF-8"));

			HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
			HttpEntity entity = response.getEntity(); // 获取响应实体

			if (null != entity) {
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity); // Consume response content
			}
			System.out.println("请求地址: " + httpPost.getURI());
			System.out.println("响应状态: " + response.getStatusLine());
			System.out.println("响应长度: " + responseLength);
			System.out.println("响应内容: " + responseContent);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
		}
		return responseContent;
	}

}
