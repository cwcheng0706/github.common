package com.zy.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;



public class HttpsInvoker extends AbstractHttps{
	
	public HttpsInvoker(String keyStorePath, String keyStorePassword, String trustKeyStorePath, String trustKeySotrePassword) {
		super(keyStorePath, keyStorePassword, trustKeyStorePath, trustKeySotrePassword);
	}

	public String get(String url) throws Exception {
		HttpsURLConnection connection = getConnection(url);
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		String line = null;
		StringBuilder sb = new StringBuilder();
		connection.setRequestMethod(C.Http.METHOD_GET);
		try {
			int code = connection.getResponseCode();
			if(code != C.Http.CODE_200 ) {
				is = connection.getErrorStream();
			}else {
				is = connection.getInputStream();
			}
			isr = new InputStreamReader(is);
			reader = new BufferedReader(isr);
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
//			if(code != C.Http.CODE_200 ) {
//				BusinessException businessException = (BusinessException) JsonUtil.jsonToBean(sb.toString(), BusinessException.class);
//				throw businessException;
//			}
		} catch (IOException e) {
			throw new IOException(e);
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
		return sb.toString();
	}

	public String post(String url) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		OutputStream os = null;
		DataOutputStream out = null;
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		HttpsURLConnection connection = getConnection(url.substring(0,url.indexOf(C.Symbol.QUESTION_MARK)) );
		try {
			/***
			 * 设置是否向connection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true,默认为false
			 */
			connection.setDoOutput(true);
			// Read from the connection. Default is true.
			connection.setDoInput(true);
			connection.setRequestMethod(C.Http.METHOD_POST);
			// Post 请求不能使用缓存
			connection.setUseCaches(false);

			// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
			connection.setInstanceFollowRedirects(true);
			/***
			 * 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
			 * 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
			 */
			connection.setRequestProperty(C.Http.CONTENT_TYPE, C.Http.APPLICATION_X_WWW_FORM_URLENCODED);

			os = connection.getOutputStream();
			out = new DataOutputStream(os);
			String content = "";
			if(-1 != url.indexOf(C.Symbol.QUESTION_MARK)) {
				content = url.substring(url.indexOf(C.Symbol.QUESTION_MARK) + 1);
			}
			// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
			out.writeBytes(content);
			out.flush();

			int code = connection.getResponseCode();
			if(code != C.Http.CODE_200 ) {
				in = connection.getErrorStream();
			}else {
				in = connection.getInputStream();
			}
			isr = new InputStreamReader(in);
			reader = new BufferedReader(isr);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
//			if(code == C.Http.CODE_400 ) {
//				BusinessException businessException = (BusinessException) JsonUtil.jsonToBean(sb.toString(), BusinessException.class);
//				
//				throw businessException;
//			}
		} catch (IOException e) {
			throw new BusinessException(e);
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
		return sb.toString();

	}
	

}
