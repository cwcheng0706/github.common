package com.test.ssl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


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
	private static final String CMD_DELETE = " -delete ";
	private static final String CMD_EXPORT = " -export ";
	private static final String CMD_IMPORT = " -import ";
	private static final String CMD_V = " -v ";
	private static final String CMD_ALIAS = " -alias ";
	private static final String CMD_KEYALG = " -keyalg ";
	private static final String CMD_KEYSTORE = " -keystore ";
	private static final String CMD_STORETYPE = " -storetype ";
	private static final String CMD_DNAME = " -dname ";
	private static final String CMD_VALIDITY = " -validity ";
	private static final String CMD_STOREPASS = " -storepass ";
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

	private static final String JMX_CREDENTIALS = "jmx.remote.credentials";

	private static byte[] blockLock = new byte[0];
	private static String jmxUrl;

	/**
	 * 以下做成可配置 monitorRole QED controlRole R&D
	 */
	private static String roleName;
	private static String rolePass;
	private static String port;
	private static String sslObjectName;

	private static Properties ssl = new Properties();

	static {
		
	}
	
	public static void init(String fileName) {
		InputStream is = null;
		try {
			
			is = new FileInputStream(fileName);
			ssl.load(is);
			if (StringUtils.isEmpty(ssl.getProperty("servercer.filepath"))) {
				ssl.put("servercer.filepath", KeytoolUtils.class.getResource("/ca/server.cer").getPath());
			}
			if (StringUtils.isEmpty(ssl.getProperty("truststore.filepath"))) {
				ssl.put("truststore.filepath", KeytoolUtils.class.getResource("/ca/client.truststore").getPath());
			}
			logger.info("加载配置文件 application.properties 成功!");
		} catch (Exception e) {
			logger.error("加载配置文件 application.properties 失败!", e);
		}

		roleName = ssl.getProperty("jmx.user.name");
		rolePass = ssl.getProperty("jmx.user.passwd");
		port = ssl.getProperty("jmx.remote.port");
		sslObjectName = ssl.getProperty("jmx.sslObjectName");
		jmxUrl = "service:jmx:rmi:///jndi/rmi://" + ssl.getProperty("cer.server.url") + ":" + port + "/jmxrmi";
	}

	/**
	 * @description: 根据会员code导入证书
	 * @Author: lishaowei
	 * @return TODO
	 */
	public static boolean importKeyStore(String sourceAlias) {
		boolean ret = true;
		ret = importKeyStore(sourceAlias, ssl.getProperty("cer.out.path") + File.separator + sourceAlias + File.separator + sourceAlias + SUFFIX_CER, ssl.getProperty("truststore.filepath"), ssl.getProperty("truststore.storepass"));

		if (ret) {
			activeSSLKeyStore();
		}
		return ret;
	}

	/**
	 * @description: 根据会员code停用证书
	 * @Author: lishaowei keytool -delete -alias client -keystore
	 *          server.truststore -storepass server123456
	 * @return TODO
	 */
	public static boolean removeCertificate(String alias) {
		boolean ret = removeCertificate(alias, ssl.getProperty("truststore.filepath"), ssl.getProperty("truststore.storepass"));
		if (ret) {
			activeSSLKeyStore();
		}
		return ret;
	}

	/**
	 * @description: 根据会员code删除证书
	 * @Author: lishaowei
	 * @return TODO
	 */
	public static boolean deleteCertificate(String sourceAlias) {
		boolean b = true;
		try {
			FileUtils.deleteDirectory(new File(ssl.getProperty("cer.out.path") + File.separator + sourceAlias));
		} catch (IOException e) {
			logger.error("删除证书失败" + e);
			b = false;
		}
		return b;
	}

	/**
	 * @param alias
	 * @param serverStrustoreFilePath
	 * @param serverStrustorePass
	 * @return TODO
	 */
	private static boolean removeCertificate(String alias, String serverStrustoreFilePath, String serverStrustorePass) {

		return exeCmd(getStrByStringArr(new String[] { CMD_KEYTOOL, CMD_DELETE, CMD_ALIAS, alias, CMD_KEYSTORE, serverStrustoreFilePath, CMD_STOREPASS, serverStrustorePass

		}));

	}

	public static int getCerValidityLimit() {
		return Integer.parseInt(ssl.getProperty("cer.validity.limit"));
	}

	public static byte[] deveCerZip(String alias, String storepass) throws FileNotFoundException, IOException {
		String cerFolder = ssl.getProperty("cer.out.path");
		String serverCerFilePath = ssl.getProperty("servercer.filepath");
		String clientKeyFolder = cerFolder + File.separator + alias;
		String cerName = alias + SUFFIX_P12;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b_sp = storepass.getBytes();
		ZipOutputStream zos = new ZipOutputStream(baos);
		try {
			zos.putNextEntry(new ZipEntry("证书密码.txt"));
			zos.write(b_sp, 0, b_sp.length);

			File serverCer = new File(serverCerFilePath);
			if (!serverCer.exists()) {
				throw new FileNotFoundException("证书文件 " + serverCer.getAbsolutePath() + "没有找到！");
			}

			File cerFile = new File(clientKeyFolder + File.separator + cerName);
			if (!cerFile.exists()) {
				throw new FileNotFoundException("证书文件 " + cerFile.getAbsolutePath() + "没有找到！");
			}

			byte[] byte1 = FileUtils.readFileToByteArray(serverCer);
			zos.putNextEntry(new ZipEntry(serverCer.getName()));
			zos.write(byte1, 0, byte1.length);

			byte[] byte2 = FileUtils.readFileToByteArray(cerFile);
			zos.putNextEntry(new ZipEntry(cerName));
			zos.write(byte2, 0, byte2.length);

			zos.flush();

		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(zos);
		}
		return baos.toByteArray();
	}

	public static boolean create(CertificateBody certificateBody, String alias, String storepass) {
		boolean ret = false;

		logger.info("============Certificate Create Start=========");
		try {
			String cerFolder = ssl.getProperty("cer.out.path");
			String validity = ssl.getProperty("cer.validity.limit");
			String truststoreStoreFilePath = ssl.getProperty("truststore.filepath");
			String truststoreStorepass = ssl.getProperty("truststore.storepass");

			certificateBody.setCn(ssl.getProperty("cer.server.url"));

			if (certificateBody.getC() == null) {
				certificateBody.setC(ssl.getProperty("cer.country.default"));
			}

			KeytoolUtils keytoolUtils = new KeytoolUtils();
			String clientKeyFolder = cerFolder + File.separator + alias;
			File clientKeyFolderFile = new File(clientKeyFolder);

			if (!clientKeyFolderFile.exists()) {
				clientKeyFolderFile.mkdirs();
			}

			// 1.生成客户 证书库 生成.p12文件
			Certificate p12Certificate = new Certificate();
			p12Certificate.setAlias(alias);
			p12Certificate.setStoreType(StoreType.PKCS12.getName());
			p12Certificate.setKeyStore(p12Certificate.getAlias());
			// p12Certificate.setValidity(validity);
			p12Certificate.setValidity("3650");
			p12Certificate.setStorePass(storepass);
			p12Certificate.setKeyPass(storepass);
			p12Certificate.setBody(certificateBody);

			ret = keytoolUtils.geneClientCertificate(p12Certificate, clientKeyFolder, truststoreStoreFilePath, truststoreStorepass);

			logger.info("============end=========");
		} catch (Exception e) {
			logger.error(e);
		}

		return ret;
	}

	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 下午4:53:21
	 * @param p12Certificate
	 * @param keyFolder
	 *            证书存放目录
	 * @return
	 */
	private boolean geneClientCertificate(Certificate p12Certificate, String keyFolder, String truststoreStoreFilePath, String truststoreStorepass) {

		boolean ret = true;

		// 1.生成客户 证书库
		logger.debug("1.生成客户 证书库");
		ret = generateKey(p12Certificate, keyFolder);

		if (ret) {
			// 2.从客户 证书库中导出客户端证书 生成.cer证书文件
			logger.debug("2.从客户 证书库中导出客户端证书 生成.cer证书文件");
			ret = exportCertificate(p12Certificate, keyFolder);
		}

		if (ret) {
			// 3.将客户端证书导入到服务器证书库
			logger.debug("3.将客户端证书导入到服务器证书库");
			String sourceCerFilePath = keyFolder + File.separator + p12Certificate.getAlias() + SUFFIX_CER;
			ret = importKeyStore(p12Certificate.getAlias(), sourceCerFilePath, truststoreStoreFilePath, truststoreStorepass);
		}

		if (ret) {
			activeSSLKeyStore();
		}

		return ret;
	}

	/**
	 * 激活写操作后的证书 使其证书生效
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月2日 下午3:57:20
	 */
	private static boolean activeSSLKeyStore() {
		boolean ret = false;
		JMXConnector connector = null;
		MBeanServerConnection connection = null;
		synchronized (blockLock) {

			try {
				connector = getConnector(jmxUrl, roleName, rolePass);
				connection = getConnection(connector);

				// 解除连接池绑定
				invokeMethod(connection, "unbind");
				logger.debug("JMX远程执行方法unbind成功！");

				// 绑定连接池
				invokeMethod(connection, "bind");
				logger.debug("JMX远程执行方法bind成功！");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}

				// 连接池开启
				invokeMethod(connection, "start");
				logger.debug("JMX远程执行连接池开启方法start成功！");
				logger.debug("激活证书成功！");
				ret = true;

			} catch (Exception e) {
				logger.error("激活证书文件异常！" + e);
			} finally {
				if (null != connector) {
					try {
						connector.close();
					} catch (IOException e) {
						logger.error("关闭连接器异常！");
					}
				}
			}

			return ret;
		}

	}

	/**
	 * 根据连接器获取JMX连接
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月2日 下午3:40:51
	 * @param connector
	 * @return
	 */
	private static MBeanServerConnection getConnection(JMXConnector connector) {

		MBeanServerConnection connection = null;

		try {
			connection = connector.getMBeanServerConnection();

		} catch (Exception e) {
			logger.error("JMX连接远程服务，生成连接异常！");
			throw new RuntimeException(e);
		}
		return connection;
	}

	/**
	 * 远程JMX调用method
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月2日 下午3:46:54
	 * @param connection
	 * @param method
	 * @return
	 */
	private static void invokeMethod(MBeanServerConnection connection, String method) {

		ObjectName threadObjName = null;
		ObjectInstance instance = null;

		try {
			threadObjName = new ObjectName(sslObjectName);
			instance = connection.getObjectInstance(threadObjName);

			connection.invoke(instance.getObjectName(), method, null, null);

		} catch (Exception e) {
			logger.error("JMX远程调用方法【" + method + "】,出现异常！");
			throw new RuntimeException(e);
		}

	}

	/**
	 * 生成远程服务连接器
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月2日 下午3:31:27
	 * @param jmxURL
	 * @param roleName
	 * @param rolePass
	 * @return
	 */
	private static JMXConnector getConnector(String jmxURL, String roleName, String rolePass) {

		JMXConnector connector = null;
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put(JMX_CREDENTIALS, new String[] { roleName.trim(), rolePass.trim() });
		JMXServiceURL serviceURL = null;

		try {
			serviceURL = new JMXServiceURL(jmxURL);
			connector = JMXConnectorFactory.connect(serviceURL, map);
		} catch (Exception e) {
			logger.error("JMX连接远程服务，生成连接器异常！");
			throw new RuntimeException(e);
		}

		return connector;
	}

	/**
	 * 将客户端证书导入到服务器证书库(使得服务器信任客户端证书) keytool -import -v -alias client -file
	 * E:\ssl\client.cer -keystore E:\ssl\server.truststore -storepass 123456
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 上午10:27:25
	 * @param sourceAlias
	 * @param sourceCerFilePath
	 *            证书文件名（包括目录+文件名）
	 * @param destKeyStoreFilePath
	 *            库文件名 （包括目录+文件名）
	 * @param storePass
	 * @return TODO
	 */
	private static boolean importKeyStore(String sourceAlias, String sourceCerFilePath, String destKeyStoreFilePath, String storePass) {
		return exeCmd(getStrByStringArr(new String[] { CMD_KEYTOOL, CMD_IMPORT, CMD_V, CMD_ALIAS, sourceAlias, CMD_FILE, sourceCerFilePath, CMD_KEYSTORE, destKeyStoreFilePath, CMD_STOREPASS, storePass, NOPROMPT }));
	}

	/**
	 * 从客户端证书库中导出客户端证书 keytool -export -alias client -keystore
	 * /root/zy/client.p12 -storetype PKCS12 -storepass 123456 -rfc -file
	 * /root/zy/client.cer
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 上午10:08:59
	 * @param certificate
	 * @param keyFolder
	 */
	private boolean exportCertificate(Certificate certificate, String keyFolder) {
		String[] cmdArr = new String[] { CMD_KEYTOOL, CMD_EXPORT, CMD_ALIAS, certificate.getAlias(), CMD_KEYSTORE, keyFolder + File.separator + certificate.getKeyStore() + SUFFIX_P12, CMD_STORETYPE, certificate.getStoreType(), CMD_STOREPASS, certificate.getStorePass(), CMD_RFC,
				CMD_FILE, keyFolder + File.separator + certificate.getAlias() + SUFFIX_CER };

		String temp = getStrByStringArr(cmdArr);
		return exeCmd(temp);
	}

	/**
	 * 生成 客户 证书库 keytool -genkey -v -alias client -keyalg RSA -storetype
	 * PKCS12/JKS -keystore /root/zy/client.p12 -dname
	 * "CN=jl.ssl.com,OU=JL_OU,O=JL_O,L=SH,ST=SH,C=CN" -validity 1 -storepass
	 * 123456 -keypass 123456
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 上午10:05:51
	 * @param certificate
	 * @param keyFolder
	 */
	private boolean generateKey(Certificate certificate, String keyFolder) {
		String dname = generateCerBody(certificate.getBody());
		String[] cmdArr = new String[] { CMD_KEYTOOL, CMD_GENKEY, "-genkey", CMD_V, CMD_ALIAS, certificate.getAlias(), CMD_KEYALG, KEYALG, CMD_STORETYPE, certificate.getStoreType(), CMD_KEYSTORE, keyFolder + File.separator + certificate.getKeyStore() + SUFFIX_P12, CMD_DNAME,
				dname, CMD_VALIDITY, certificate.getValidity(), CMD_STOREPASS, certificate.getStorePass(), CMD_KEYPASS, certificate.getKeyPass() };
		String temp = getStrByStringArr(cmdArr);
		return exeCmd(temp);

	}

	private static String getStrByStringArr(String[] strArr) {
		String temp = "";
		for (int i = 0; i < strArr.length; i++) {
			temp += strArr[i];
		}
		return temp;
	}

	/**
	 * 生成证书信息主体
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月14日 下午1:52:02
	 * @param body
	 * @return
	 */
	private String generateCerBody(CertificateBody body) {
		StringBuilder sb = new StringBuilder();
		sb.append(CN).append(body.getCn()).append(",").append(OU).append(body.getOu()).append(",").append(O).append(body.getO()).append(",").append(L).append(body.getL()).append(",").append(ST).append(body.getSt()).append(",").append(C).append(body.getC());

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
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月12日 上午11:34:59
	 * @param cmd
	 */
	public static boolean exeCmd(String cmd) {
		boolean ret = true;
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		Process p = null;
		try {
			logger.debug("cmd:" + cmd);
			p = run.exec(cmd);
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
					logger.error("命令执行失败!【" + cmd + "】");
					ret = false;
				}

			}
		} catch (Exception e) {
			ret = false;
			logger.error("执行cmd异常：" + e);
		}
		return ret;
	}

}
