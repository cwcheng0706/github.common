package com.zy.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpConnectionInvoke {

	public static void main(String[] args) throws Exception {

		ExecutorService executos = Executors.newFixedThreadPool(100);

		while (true) {
			for (int i = 0; i < 1000; i++) {
				Runnable command = new Runnable() {
					public void run() {
						try {
//							HttpConnectionInvoke.readContentFromPost("http://119.63.35.126/");
//							HttpConnectionInvoke.readContentFromPost("http://kk.mastao.com//feedback_261dd722.aspx?age=");
							HttpConnectionInvoke.readContentFromPost("http://aa.akls.com.cn/prodetail.php?id=149");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				executos.execute(command);
			}
			Thread.sleep(55);
		}

	}

	public static void readContentFromGet(String url) throws IOException {
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		String getURL = url + "?username=" + URLEncoder.encode("fat man", "utf-8");
		URL getUrl = new URL(getURL);
		/***
		 * 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
		 * 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
		 */
		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
		// 服务器
		connection.connect();

		// 取得输入流，并使用Reader读取
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		System.out.println("=============================");
		System.out.println("Contents of get request");
		System.out.println("=============================");
		String lines;
		try {
			while ((lines = reader.readLine()) != null) {
				System.out.println(lines);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				reader.close();
				reader = null;
			}
			if (null != isr) {
				isr.close();
				isr = null;
			}
			if (null != is) {
				is.close();
				is = null;
			}

			// 断开连接
			connection.disconnect();
		}

		System.out.println("=============================");
		System.out.println("Contents of get request ends");
		System.out.println("=============================");
	}

	public static void readContentFromPost(String url) throws IOException {
		// Post请求的url，与get不同的是不需要带参数
		URL postUrl = new URL(url);
		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		/***
		 * 设置是否向connection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true,默认为false
		 */
		connection.setDoOutput(true);
		// Read from the connection. Default is true.
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		// Post 请求不能使用缓存
		connection.setUseCaches(false);

		// URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
		// connection.setFollowRedirects(true);

		// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
		connection.setInstanceFollowRedirects(true);
		/***
		 * 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		 * 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
		 */
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		OutputStream os = null;
		DataOutputStream out = null;

		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			/***
			 * 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
			 * 要注意的是connection.getOutputStream会隐含的进行connect。
			 */
			connection.connect();

			os = connection.getOutputStream();
			out = new DataOutputStream(os);
			// 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
			String content = "firstname=" + URLEncoder.encode("一个大肥人", "utf-8");
			// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
			out.writeBytes(content);
			out.flush();

			in = connection.getInputStream();
			isr = new InputStreamReader(in);
			reader = new BufferedReader(isr);
			String line;
			System.out.println("=============================");
			System.out.println("Contents of post request");
			System.out.println("=============================");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println("=============================");
			System.out.println("Contents of post request ends");
			System.out.println("=============================");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				out.close();
				out = null;
			}
			if (null != os) {
				os.close();
				os = null;
			}
			if (null != reader) {
				reader.close();
				reader = null;
			}
			if (null != isr) {
				isr.close();
				isr = null;
			}
			if (null != in) {
				in.close();
				in = null;
			}

			connection.disconnect();
		}

	}

	public static void readContentFromChunkedPost(String url) throws IOException {
		URL postUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		/***
		 * 与readContentFromPost()最大的不同，设置了块大小为5字节
		 */
		connection.setChunkedStreamingMode(5);
		connection.connect();

		OutputStream os = null;
		DataOutputStream out = null;

		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			/***
			 * 注意，下面的getOutputStream函数工作方式于在readContentFromPost()里面的不同
			 * 在readContentFromPost()里面该函数仍在准备http request，没有向服务器发送任何数据
			 * 而在这里由于设置了ChunkedStreamingMode，getOutputStream函数会根据connect之前的配置
			 * 生成http request头，先发送到服务器。
			 */
			os = connection.getOutputStream();
			out = new DataOutputStream(os);
			String content = "firstname=" + URLEncoder.encode("一个大肥人 " + " " + "asdfasfdasfasdfaasdfasdfasdfdasfs", "utf-8");
			out.writeBytes(content);
			out.flush();
			
			// request了，而在readContentFromPost()函数里，要等到下一句服务器才能收到http请求。
			is = connection.getInputStream();
			isr = new InputStreamReader(is);
			reader = new BufferedReader(isr);
			
			String line;
			System.out.println("=============================");
			System.out.println("Contents of post request");
			System.out.println("=============================");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println("=============================");
			System.out.println("Contents of post request ends");
			System.out.println("=============================");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != out) {
				out.close();
				out = null;
			}
			if(null != os) {
				os.close();
				os = null;
			}
			
			if(null != reader) {
				reader.close();
				reader = null;
			}
			if(null != isr) {
				isr.close();
				isr = null;
			}
			if(null != is) {
				is.close();
				is = null;
			}
			connection.disconnect();
		}

	}
}
