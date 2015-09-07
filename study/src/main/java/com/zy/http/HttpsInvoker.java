package com.zy.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;



public class HttpsInvoker extends AbstractHttps{
	
	public HttpsInvoker(String keyStorePath, String keyStorePassword, String trustKeyStorePath, String trustKeySotrePassword) {
		super(keyStorePath, keyStorePassword, trustKeyStorePath, trustKeySotrePassword);
	}
	
	public HttpsInvoker(InputStream keyStoreInputStream, String keyStorePassword, InputStream trustKeyStoreInputStream, String trustKeySotrePassword) {
		super(keyStoreInputStream, keyStorePassword, trustKeyStoreInputStream, trustKeySotrePassword);
	}

	public String get(String url) throws Exception {
		String ret = "";
		HttpsURLConnection connection = getConnection(url);
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		
		BufferedInputStream bis = null;
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		
		String line = null;
		StringBuilder sb = new StringBuilder();
		connection.setRequestMethod(C.Http.METHOD_GET);
		try {
			int code = connection.getResponseCode();
			System.out.println("返回码【" + code + "】");
			if(code != C.Http.CODE_200 ) {
				is = connection.getErrorStream();
			}else {
				is = connection.getInputStream();
			}
			
			String contentType = connection.getHeaderField("Content-Type");
			if(C.Http.CONTENT_TYPE_JSON.equals(contentType)) {
				isr = new InputStreamReader(is,C.Charset.UTF8);
				reader = new BufferedReader(isr);
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				ret = sb.toString();
				System.out.println(ret);
//				if(-1 != ret.indexOf("content")) {
//					ret = JsonUtil.getJsonValue(sb.toString(), "content").toString();
//				}
			}else if(C.Http.CONTENT_TYPE_PDF.equals(contentType)) {
				File file = new File("respons.pdf");
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
			}
			if(code != C.Http.CODE_200 ) {
				BusinessException businessException = (BusinessException) JsonUtil.jsonToBean(ret, BusinessException.class);
				throw businessException;
			}
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
			
			
			if (null != bos) {
				bos.close();
				bos = null;
			}
			if (null != os) {
				os.close();
				os = null;
			}
			if (null != bis) {
				bis.close();
				bis = null;
			}
			
			// 断开连接
			connection.disconnect();
		}
		return ret;
	}
	
	public byte[] get4Byte(String url) throws Exception {
		byte[] ret = null;
		HttpsURLConnection connection = getConnection(url);
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		
		BufferedInputStream bis = null;
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		
		ByteArrayOutputStream baos = null;
		
		connection.setRequestMethod(C.Http.METHOD_GET);
		try {
			int code = connection.getResponseCode();
			System.out.println("返回码【" + code + "】");
			if(code != C.Http.CODE_200 ) {
				is = connection.getErrorStream();
			}else {
				is = connection.getInputStream();
			}
			
			String contentType = connection.getHeaderField("Content-Type");
			if(C.Http.CONTENT_TYPE_PDF.equals(contentType)) {
				baos = new ByteArrayOutputStream();
				bis = new BufferedInputStream(is);
				
				byte[] buffer = new byte[2048];
				int len = 0;
				while(-1 != (len = bis.read(buffer))) {
					baos.write(buffer,0,len);
				}
				baos.flush();
				ret = baos.toByteArray();
			}
			if(code != C.Http.CODE_200 ) {
				BusinessException businessException = (BusinessException) JsonUtil.jsonToBean(new String(baos.toByteArray(),C.Charset.UTF8), BusinessException.class);
				throw businessException;
			}
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
			
			
			if (null != bos) {
				bos.close();
				bos = null;
			}
			if (null != os) {
				os.close();
				os = null;
			}
			if (null != bis) {
				bis.close();
				bis = null;
			}
			
			// 断开连接
			connection.disconnect();
		}
		return ret;
	}

	public String post(String url) throws Exception {
		String ret = "";
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
			System.out.println("返回码【" + code + "】");
			if(code != C.Http.CODE_200 ) {
				in = connection.getErrorStream();
			}else {
				in = connection.getInputStream();
			}
			String contentType = connection.getHeaderField("Content-Type");
			if(C.Http.CONTENT_TYPE_JSON.equals(contentType)) {
				isr = new InputStreamReader(in,C.Charset.UTF8);
				reader = new BufferedReader(isr);
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				ret = sb.toString();
				System.out.println(ret);
//				if(-1 != ret.indexOf("content")) {
//					ret = JsonUtil.getJsonValue(sb.toString(), "content").toString();
//				}
			}else if(C.Http.CONTENT_TYPE_PDF.equals(contentType)) {
				
			}
			if(code == C.Http.CODE_400 ) {
				BusinessException businessException = (BusinessException) JsonUtil.jsonToBean(sb.toString(), BusinessException.class);
				throw businessException;
			}
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
		return ret;

	}
	

}
