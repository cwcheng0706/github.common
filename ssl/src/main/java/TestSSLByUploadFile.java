import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class TestSSLByUploadFile {

	private static final String KEY_STORE_TYPE_JKS = "jks";
	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	private static final String SCHEME_HTTPS = "https";

	// https端口
	private static final int HTTPS_PORT = 8443;

	// 请求的接口地址post方式url
	private static final String HTTPS_URL = "https://jl.ssl.com:8443/poseidon-atlantis/upload.do" ;
	
	// 请求的接口地址get方式url

	// 用户申请的证书
	private static final String KEY_STORE_CLIENT_PATH = "C:\\ssl\\client.p12";
	// p12文件的密码在用户下载包里面的txt文件中
	private static final String KEY_STORE_PASSWORD = "client123456";

	// 用户下载包里面服务端证书文件（server.cer） 需要按文档步骤生成服务端的信任证书库文件
	private static final String KEY_STORE_TRUST_PATH = "C:\\ssl\\server.truststore";
	// 在作生成server.truststore操作时自己设置的密码
	private static final String KEY_STORE_TRUST_PASSWORD = "client123456";

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
	public static String ssl() throws Exception {
		String ret = "";
		HttpClient httpClient = new DefaultHttpClient();
		
		InputStreamReader isr = null;
		BufferedReader bufferedReader = null;
		
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
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
//	        nvps.add(new BasicNameValuePair("timestamp", "2014-09-23 18:30:03"));  
//	        nvps.add(new BasicNameValuePair("v", "1.0"));  
//	        nvps.add(new BasicNameValuePair("serial_no", "9910013000620000046"));
//	        nvps.add(new BasicNameValuePair("name", "test"));
//	        nvps.add(new BasicNameValuePair("certiNum", "123456"));
//	        nvps.add(new BasicNameValuePair("financeProductId", "10001"));
//	        nvps.add(new BasicNameValuePair("orderAmt", "25000"));
//	        post.setEntity(new UrlEncodedFormEntity(nvps)); 
	        
	        
	        MultipartEntity multipartEntity = new MultipartEntity();
	        FileBody file = new FileBody(new File("c:\\zhuyong.pfx"));  
			FormBodyPart bodyPart = new FormBodyPart("file", file);
			multipartEntity.addPart(bodyPart);
			
			
			ContentBody contentBody =  new StringBody("朱勇",Charset.forName("UTF-8"));  
			multipartEntity.addPart("userName", contentBody );
	        
	        post.setEntity(multipartEntity); 
	        
			HttpResponse response = httpClient.execute(post);
			
			//get方式
//			HttpGet get = new HttpGet(HTTPS_URL);
//			HttpResponse response = httpClient.execute(get);
			
			HttpEntity respEntity = response.getEntity();


			StringBuffer sb = new StringBuffer();
			String text = "";
			if (respEntity != null) {
				System.out.println("Response content length: " + respEntity.getContentLength());
				isr = new InputStreamReader(respEntity.getContent(), "UTF-8");
				bufferedReader = new BufferedReader(isr);
				while ((text = bufferedReader.readLine()) != null) {
					sb.append(text);
				}
				
				ret = sb.toString();

			}
			EntityUtils.consume(respEntity);
			
			System.out.println("-------------------响应start---------------------");
			System.out.println(ret);
			System.out.println("-------------------响应end---------------------");
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			httpClient.getConnectionManager().shutdown();
			
		}

		return ret;
	}

}
