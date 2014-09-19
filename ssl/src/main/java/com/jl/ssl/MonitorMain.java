package com.jl.ssl;

import java.io.File;
import java.io.FileInputStream;

import com.jl.ssl.util.HttpClientUtils;

public class MonitorMain {

	
	/**
	 * java -jar ssl.jar "401.p12" "bz65ne0wlfv0" "server.truststore" "IshmwQT7" "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.get&timestamp=2014-08-25 17:15:03&format=json&v=1.0&orderCodes=2014010000139&serial_no=9900002100000110519"
	 * java -jar ssl.jar "401.p12" "bz65ne0wlfv0" "server.truststore" "IshmwQT7" "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.cancel&timestamp=2014-08-25 17:30:03&format=json&v=1.0&orderCode=2014010000161&serial_no=9900002100000110516" "post"
	 * https://open.jlfex.com:8443/atlantis/rest?method=jl.file.get&timestamp=2014-08-26%2008:58:03&format=json&serial_no=9900003000610000026&v=1.0&fileId=83054
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月25日 下午4:27:06
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			System.out.println("========================");
			
			if(null == args || 5 > args.length) {
				System.out.println("传入的参数个数不对！");
				return;
			}
			
			
			String ksFilePath = args[0]; //"C:\\Users\\Administrator\\Desktop\\client.p12";
			String clientStroePass = args[1]; //"client123456";

			String tsFilePath = args[2]; //"C:\\Users\\Administrator\\Desktop\\client.truststore";
			String serverTrusStorePass = args[3] ; //"server123456";
			String url = args[4]  ;
			
//			String ksFilePath = "C:\\https\\401.p12";
//			String clientStroePass = "bz65ne0wlfv0";
//
//			String tsFilePath = "C:\\https\\server.truststore";
//			String serverTrusStorePass = "IshmwQT7";
//			String url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.get&timestamp=2014-08-25 16:25:03&format=json&v=1.0&orderCodes=2014010000139&serial_no=9900002100000000011" ;
//			       url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.get&timestamp=2014-08-25 17:15:03&format=json&v=1.0&orderCodes=2014010000139&  serial_no=9900002100000110512";
//				   url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.order.cancel&timestamp=2014-08-26 08:58:03&format=json&v=1.0&orderCode=2014010000191&serial_no=9900002100000110516";
//				   url = "https://open.jlfex.com:8443/atlantis/rest?method=jl.file.get&timestamp=2014-08-26 09:50:03&format=json&serial_no=9910003000620000046&v=1.0&fileId=83054";
			
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
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
