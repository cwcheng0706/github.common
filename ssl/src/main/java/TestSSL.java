import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class TestSSL {

	private static final String KEY_STORE_TYPE_JKS = "jks";
	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	private static final String SCHEME_HTTPS = "https";

	// https端口
	private static final int HTTPS_PORT = 443;

	// 请求的接口地址
	private static final String HTTPS_URL = "https://open.jlfex.com/test/rest?method=jl.order.do&timestamp=2014-09-23%2017:30:03&v=1.0&serial_no=99100030006200000461&name=test&certiNum=123456&financeProductId=10001&orderAmt=25000";

	// 用户申请的证书
	private static final String KEY_STORE_CLIENT_PATH = "C:\\testssl\\006.p12";
	// p12文件的密码在用户下载包里面的txt文件中
	private static final String KEY_STORE_PASSWORD = "3ojd8b8fz9dj";

	// 用户下载包里面服务端证书文件（server.cer） 需要按文档步骤生成服务端的信任证书库文件
	private static final String KEY_STORE_TRUST_PATH = "C:\\testssl\\server.truststore";
	// 在作生成server.truststore操作时自己设置的密码
	private static final String KEY_STORE_TRUST_PASSWORD = "IshmwQT7";

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

			// HttpGet get = new HttpGet(HTTPS_URL);
			HttpPost post = new HttpPost(HTTPS_URL);
			StringEntity reqEntity = new StringEntity("<html>你好啊啊</html>", "UTF-8");
			post.setEntity(reqEntity);

			HttpResponse response = httpClient.execute(post);
			HttpEntity respEntity = response.getEntity();

			System.out.println(response.getStatusLine());

			StringBuilder sb = new StringBuilder();
			if (respEntity != null) {
				System.out.println("Response content length: " + respEntity.getContentLength());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(respEntity.getContent(), "UTF-8"));
				String text;

				while ((text = bufferedReader.readLine()) != null) {
					sb.append(text);
				}
				bufferedReader.close();

			}
			System.out.println("----------------------------------------");
			ret = sb.toString();
			System.out.println("----------------------------------------");
			EntityUtils.consume(respEntity);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return ret;
	}

}
