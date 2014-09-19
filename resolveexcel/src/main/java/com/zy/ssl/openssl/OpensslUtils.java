//package com.zy.ssl.openssl;
//
//
//import org.apache.log4j.Logger;
//
//
///**
// * @Project: 
// * @Author zy
// * @Company: JL
// * @Create Time: 2014年8月14日 上午11:33:35
// */
//public class OpensslUtils {
//	
//	private static final String SUFFIX_KEY = ".key";
//	private static final String suffix_csr = ".csr";
//	private static final String suffix_crt = ".crt";
//	private static final String batch = " -batch ";
//	
//	private String caPassword = "ca123456";
//	
//	private static Logger logger = Logger.getLogger(OpensslUtils.class);
//
//	/**
//	 * openssl genrsa -passout pass:user123456 -out user/user.key 2048
//	 * 
//	 * @Author zy
//	 * @Company: JL
//	 * @Create Time: 2014年8月18日 下午3:20:44
//	 */
//	private void genrsa(String user,String userPassword ,String certFoler) {
//		
////		String[] genrsaArr = new String[]{
////				" openssl genrsa",
////				" -passout pass:" + userPassword,
////				" -out " + certFoler+ File.separator + user. user.key 2048 "
////		};
//		
//	}
//}
