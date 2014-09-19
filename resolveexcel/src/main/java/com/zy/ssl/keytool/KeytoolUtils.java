package com.zy.ssl.keytool;

import java.io.File;

import org.apache.log4j.Logger;

import com.zy.ssl.keytool.entity.Certificate;
import com.zy.ssl.keytool.entity.CertificateBody;
import com.zy.ssl.keytool.entity.StoreType;

/**
 * @Project: 
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年8月14日 上午11:33:35
 */
public class KeytoolUtils {
	
	private static Logger logger = Logger.getLogger(KeytoolUtils.class);

	private static final String CMD_KEYTOOL = " keytool ";
	private static final String CMD_GENKEY = " -genkey ";
	private static final String CMD_EXPORT = " -export ";
	private static final String CMD_IMPORT = " -import ";
	private static final String CMD_V = " -v ";
	private static final String CMD_ALIAS = " -alias ";
	private static final String CMD_KEYALG = " -keyalg ";
	private static final String CMD_KEYSTORE = " -keystore ";
	private static final String CMD_STORETYPE = " -storetype ";
	private static final String CMD_DNAME = " -dname ";
	private static final String CMD_VALIDITY  = " -validity ";
	private static final String CMD_STOREPASS  = " -storepass ";
	private static final String CMD_KEYPASS = " -keypass ";
	private static final String CMD_RFC = " -rfc ";
	private static final String CMD_FILE = " -file ";
	
	private static final String KEYALG = "RSA";
	
	public static final String CN = "CN=";
	public static final String OU = "OU=";
	public static final String O = "O=";
	public static final String L = "L=";
	public static final String ST = "ST=";
	public static final String C = "C=";
	
	
	private static final String SUFFIX_CER = ".cer";
	private static final String SUFFIX_P12 = ".p12";
	private static final String NOPROMPT = " -noprompt ";
	
	private String serverKeyStore = " c:\\ssl\\server\\server.keystore ";
	private String serverTruststore = " c:\\ssl\\server\\client.truststore ";
	private String serverStorepass = " server123456 ";
	
	
	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 下午4:53:21
	 * @param p12Certificate
	 * @param keyFolder 证书存放目录
	 * @return
	 */
	public boolean geneClientCertificate(Certificate p12Certificate,String keyFolder) {
		
		boolean ret = true;
		
		//1.生成客户 证书库
		logger.debug("1.生成客户 证书库");
		generateKey(p12Certificate,keyFolder);
		
		//2.从客户 证书库中导出客户端证书 生成.cer证书文件
		logger.debug("2.从客户 证书库中导出客户端证书 生成.cer证书文件");
		exportCertificate(p12Certificate,keyFolder);
		
		//3.将客户端证书导入到服务器证书库
		logger.debug("3.将客户端证书导入到服务器证书库");
		String sourceCerFilePath = keyFolder + File.separator + p12Certificate.getAlias() + SUFFIX_CER;
		String destKeyStoreFilePath = this.getServerTruststore();
		String serverStorePass = this.getServerStorepass();
		importKeyStore(p12Certificate.getAlias(), sourceCerFilePath, destKeyStoreFilePath , serverStorePass);
		
		return ret;
	}
	
	
	/**
	 * 将客户端证书导入到服务器证书库(使得服务器信任客户端证书)
	 * keytool -import -v -alias client -file E:\ssl\client.cer -keystore E:\ssl\server.truststore   -storepass 123456
	 *
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 上午10:27:25
	 * @param sourceAlias
	 * @param sourceCerFilePath 证书文件名（包括目录+文件名）
	 * @param destKeyStoreFilePath 库文件名 （包括目录+文件名）
	 * @param storePass
	 */
	private void importKeyStore(String sourceAlias,String sourceCerFilePath,String destKeyStoreFilePath,String storePass) {
		String[] cmdArr = new String[]{
				CMD_KEYTOOL,
				CMD_IMPORT,
				CMD_V,
				CMD_ALIAS,sourceAlias,
				CMD_FILE,sourceCerFilePath,
				CMD_KEYSTORE,destKeyStoreFilePath,
				CMD_STOREPASS,storePass,
				NOPROMPT
		};
		
		String temp= getStrByStringArr(cmdArr);
		
		exeCmd(temp);
	}
	
	
	/**
	 * 从客户端证书库中导出客户端证书
	 * keytool -export -alias client -keystore /root/zy/client.p12 -storetype PKCS12 -storepass 123456 -rfc -file /root/zy/client.cer 
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 上午10:08:59
	 * @param certificate
	 * @param keyFolder
	 */
	private void exportCertificate(Certificate certificate,String keyFolder){
		String[] cmdArr = new String[]{
				CMD_KEYTOOL,
				CMD_EXPORT,
				CMD_ALIAS,certificate.getAlias(),
				CMD_KEYSTORE,keyFolder + File.separator + certificate.getKeyStore() + SUFFIX_P12,
				CMD_STORETYPE,certificate.getStoreType(),
				CMD_STOREPASS,certificate.getStorePass(),
				CMD_RFC,
				CMD_FILE,keyFolder + File.separator + certificate.getAlias() + SUFFIX_CER
		};
		
		String temp = getStrByStringArr(cmdArr);
		
		exeCmd(temp);
	}
	
	
	/**
	 * 生成 客户 证书库
	 * keytool -genkey -v -alias client -keyalg RSA -storetype PKCS12/JKS -keystore /root/zy/client.p12  -dname "CN=jl.ssl.com,OU=JL_OU,O=JL_O,L=SH,ST=SH,C=CN" -validity 1 -storepass 123456 -keypass 123456 
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 上午10:05:51
	 * @param certificate
	 * @param keyFolder
	 */
	private void generateKey(Certificate certificate,String keyFolder){
		
		String dname = generateCerBody(certificate.getBody());
		
		String[] cmdArr = new String[]{
				CMD_KEYTOOL,
				CMD_GENKEY,
				"-genkey",
				CMD_V,
				CMD_ALIAS,certificate.getAlias(),
				CMD_KEYALG,KEYALG,
				CMD_STORETYPE,certificate.getStoreType(),
				CMD_KEYSTORE,keyFolder + File.separator + certificate.getKeyStore() + SUFFIX_P12,
				CMD_DNAME,dname,
				CMD_VALIDITY,certificate.getValidity(),
				CMD_STOREPASS,certificate.getStorePass(),
				CMD_KEYPASS,certificate.getKeyPass()
		};
		
		
		String temp = getStrByStringArr(cmdArr);
		
		exeCmd(temp);
		
	}
	
	private String getStrByStringArr(String[] strArr) {
		String temp= "";
		for(int i = 0 ; i < strArr.length;i++) {
			temp += strArr[i];
		}
		return temp;
	}
	
	/**
	 * 生成证书信息主体
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 下午1:52:02
	 * @param body
	 * @return
	 */
	private String generateCerBody(CertificateBody body) {
		StringBuilder sb = new StringBuilder();
		sb.append(CN).append(body.getCn()).append(",")
		  .append(OU).append(body.getOu()).append(",")
		  .append(O).append(body.getO()).append(",")
		  .append(L).append(body.getL()).append(",")
		  .append(ST).append(body.getSt()).append(",")
		  .append(C).append(body.getC());
		
		return sb.toString();
	}
	
	public boolean exeCmd(String[] cmdArr) {
		boolean ret = true;
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		Process p = null;
		try {
			p = run.exec(cmdArr);
			logger.debug(cmdArr);
			
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
					logger.error("命令执行失败!");
					ret = false;
				}
				
			}
		} catch (Exception e) {
			ret = false;
			logger.error("执行cmd异常：" + e);
		}
		return ret;
	}
	

	/**
	 * 执行CMD
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月12日 上午11:34:59
	 * @param cmd
	 */
	public boolean exeCmd(String cmd) {
		boolean ret = true;
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		Process p = null;
		try {
			p = run.exec(cmd);
			logger.debug(cmd);
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
					logger.error("命令执行失败!");
					ret = false;
				}
				
			}
		} catch (Exception e) {
			ret = false;
			logger.error("执行cmd异常：" + e);
		}
		return ret;
	}


	public static void main(String[] args) {
		String keyFolder = "c:";
		KeytoolUtils keytoolUtils = new KeytoolUtils();
		
		String cn = "jl.ssl.com";
		String ou = "JL_OU";
		String o = "SH_o";
		String l = "SH_l";
		String st = "SH_st";
		String c = "CN";
		
		CertificateBody certificateBody = new CertificateBody();
		certificateBody.setCn(cn);
		certificateBody.setOu(ou);
		certificateBody.setO(o);
		certificateBody.setL(l);
		certificateBody.setSt(st);
		certificateBody.setC(c);
		
		//1.生成客户 证书库  生成.p12文件
		Certificate p12Certificate = new Certificate();
		p12Certificate.setAlias("client_1");
		p12Certificate.setStoreType(StoreType.PKCS12.getName());
		p12Certificate.setKeyStore(p12Certificate.getAlias());
		p12Certificate.setValidity("1");
		p12Certificate.setStorePass(p12Certificate.getAlias() + "123456");
		p12Certificate.setKeyPass(p12Certificate.getAlias() + "123456");
		p12Certificate.setBody(certificateBody);
		
		keytoolUtils.geneClientCertificate(p12Certificate,keyFolder);
	}
	
	

	public String getServerKeyStore() {
		return serverKeyStore;
	}


	public void setServerKeyStore(String serverKeyStore) {
		this.serverKeyStore = serverKeyStore;
	}


	public String getServerStorepass() {
		return serverStorepass;
	}



	public void setServerStorepass(String serverStorepass) {
		this.serverStorepass = serverStorepass;
	}


	public String getServerTruststore() {
		return serverTruststore;
	}


	public void setServerTruststore(String serverTruststore) {
		this.serverTruststore = serverTruststore;
	}
}
