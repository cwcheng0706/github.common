import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.jl.ssl.util.HttpClientUtils;

public class TestSSLByFile {
	
	private static final String CONTENT_TYPE_PDF = "application/pdf";
	private static final String CONTENT_TYPE_JSON = "application/json";

	private static final String KEY_STORE_TYPE_JKS = "jks";
	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	private static final String SCHEME_HTTPS = "https";
	
	private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>();

	static {
		CONTENT_TYPE_MAP.put(CONTENT_TYPE_PDF, ".pdf");
	}

	// https端口
	private static final int HTTPS_PORT = 443;

	// 请求的接口地址post方式url
	private static final String HTTPS_URL = "";
	
	// 请求的接口地址get方式url

	// 用户申请的证书
	private static final String KEY_STORE_CLIENT_PATH = "C:\\ssl\\product-test\\006\\006.p12";
	// p12文件的密码在用户下载包里面的txt文件中
	private static final String KEY_STORE_PASSWORD = "111";

	// 用户下载包里面服务端证书文件（server.cer） 需要按文档步骤生成服务端的信任证书库文件
	private static final String KEY_STORE_TRUST_PATH = "C:\\ssl\\product-test\\006\\client.truststore";
	// 在作生成server.truststore操作时自己设置的密码
	private static final String KEY_STORE_TRUST_PASSWORD = "server123456";

	public static void main(String[] args) throws Exception {
		ssl();
	}

	/**
	 * 发送https请求
	 * 
	 * @param url
	 * @param xmlStr
	 * @return
	 */
	public static void ssl() throws Exception {
		FileInputStream ksIn = new FileInputStream(new File(KEY_STORE_CLIENT_PATH));
		FileInputStream tsIn = new FileInputStream(new File(KEY_STORE_TRUST_PATH));
		
		String respStr = ssl(ksIn, KEY_STORE_PASSWORD, tsIn, KEY_STORE_TRUST_PASSWORD,HTTPS_URL,"get",HTTPS_PORT);
		System.out.println("===================响应字串===================");
		System.out.println("【" + respStr + "】");
	}
	
	
	private static String ssl(InputStream ksIn, String ksPasswd, InputStream tsIn, String tsPasswd, String url,
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
			socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
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
		System.out.println("ContentType【" + contentType + "】");
		return contentType;
	}


}
