package com.test.ssl;

public class SSLMain {

	public static void main(String[] args) throws Exception {
		if(3 != args.length) {
			System.out.println("输入参数个数不对");
		}
		
		
		
//		String file = "file:\\E:aaa";
//		System.out.println(file.substring("file:\\".length(), file.length()));
		
		String alias = args[0];
		String storepass = args[1];
		
		CertificateBody certificateBody = new CertificateBody();
		
		KeytoolUtils.init(args[2]);
		KeytoolUtils.create(certificateBody , alias , storepass);
	}
}
