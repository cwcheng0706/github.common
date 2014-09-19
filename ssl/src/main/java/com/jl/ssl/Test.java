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
//		String url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.get&timestamp=2014-08-25 16:25:03&format=json&v=1.0&orderCodes=2014010000139&serial_no=9900002100000000011" ;
//		       url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.get&timestamp=2014-08-25 17:15:03&format=json&v=1.0&orderCodes=2014010000139&  serial_no=9900002100000110512";
//			   url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.cancel&timestamp=2014-08-26 08:58:03&format=json&v=1.0&orderCode=2014010000191&serial_no=9900002100000110516";
//			   url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.file.get&timestamp=2014-08-26 09:50:03&format=json&serial_no=9910003000620000046&v=1.0&fileId=83054";
		
		String url = "https://open.jlfex.com/test/rest?method=jl.financefroduct.get&pageSize=10&pageNum=1&timestamp=2014-09-15%2011:50:03&serial_no=9910003000620000046&v=1.0";
		
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
