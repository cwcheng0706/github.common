package com.jl.ssl;

import java.io.File;
import java.io.FileInputStream;

import com.jl.ssl.util.HttpClientUtils;

public class Test {

	public static void main(String[] args) throws Exception{

//		String ksFilePath = args[0]; //"C:\\Users\\Administrator\\Desktop\\client.p12";
//		String clientStroePass = args[1]; //"client123456";
//
//		String tsFilePath = args[2]; //"C:\\Users\\Administrator\\Desktop\\client.truststore";
//		String serverTrusStorePass = args[3] ; //"server123456";
//		String url = args[4]  ;
		
		String ksFilePath = "C:\\006.p12";
		String clientStroePass = "3ojd8b8fz9dj";
//
		String tsFilePath = "C:\\server.truststore";
		String serverTrusStorePass = "123456";
		
		String url = "";
		
		url = url.replace(" ", "%20");
		
		String method = "get";
		if(6 == args.length && null != args[5] && !"".equals(args[5].trim())) {
			method = args[5];
		}
		
		FileInputStream ksIn = new FileInputStream(new File(ksFilePath));
		FileInputStream tsIn = new FileInputStream(new File(tsFilePath));
		
		String respStr = HttpClientUtils.ssl(ksIn, clientStroePass, tsIn, serverTrusStorePass,url,method,443);
		System.out.println("===================响应字串===================");
		System.out.println("【" + respStr + "】");
	}

}
