package com.zy.jmx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程控制tomcat server.xml中的Connector 节点。 由于 在配置 tomcat+keytool(无openssl) 双向认证。在导入客户端证书不能生效。故用JMX重启SSL中的8443端口对应的连接池
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年9月2日 下午4:19:07
 */
public class JMXService {

	private static final Logger logger = LoggerFactory.getLogger(JMXService.class);

	private static final String JMX_CREDENTIALS = "jmx.remote.credentials";

	private String jmxUrl;

	/**
	 * 以下做成可配置 monitorRole QED controlRole R&D
	 */
	private String roleName = "monitorRole";
	private String rolePass = "QED";
	private String host = "localhost";
	private String port = "9008";
	private String sslObjectName = "Catalina:type=ThreadPool,name=\"http-bio-8443\"";

	public JMXService() {
		jmxUrl = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
	}

	public static void main(String[] args) throws Exception {

		JMXService jmxService = new JMXService();
		jmxService.activeSSLKeyStore();

	}

	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月2日 下午3:57:20
	 */
	public synchronized void activeSSLKeyStore() {
		JMXConnector connector = getConnector(jmxUrl, roleName, rolePass);

		try {
			MBeanServerConnection connection = getConnection(connector);

			// 解除连接池绑定
			invokeMethod(connection, "unbind");
			logger.debug("JMX远程执行方法unbind成功！");

			// 绑定连接池
			invokeMethod(connection, "bind");
			logger.debug("JMX远程执行方法bind成功！");
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}

			// 连接池开启
			invokeMethod(connection, "start");
			logger.debug("JMX远程执行连接池开启方法start成功！");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (null != connector) {
				try {
					connector.close();
				} catch (IOException e) {
					logger.error("关闭连接器异常！");
				}
			}
		}
		logger.debug("激活证书成功！");
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
	private JMXConnector getConnector(String jmxURL, String roleName, String rolePass) {

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
	 * 根据连接器获取JMX连接
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月2日 下午3:40:51
	 * @param connector
	 * @return
	 */
	private MBeanServerConnection getConnection(JMXConnector connector) {

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
	private void invokeMethod(MBeanServerConnection connection, String method) {

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

	// private void getConnection() {
	//
	// // 用户名、密码
	// Map<String, String[]> map = new HashMap<String, String[]>();
	// map.put("jmx.remote.credentials", new String[] { "monitorRole", "QED" });
	// String jmxURL = "service:jmx:rmi:///jndi/rmi://localhost:9007/jmxrmi";
	//
	// JMXServiceURL serviceURL = null;
	//
	// JMXConnector connector = null;
	// MBeanServerConnection mbsc = null;
	//
	// try {
	//
	// serviceURL = new JMXServiceURL(jmxURL);// MalformedURLException
	// connector = JMXConnectorFactory.connect(serviceURL, map); // IOException
	// mbsc = connector.getMBeanServerConnection();
	//
	// ObjectName threadObjName = new ObjectName(sslObjectName); //
	// MalformedObjectNameException
	// ObjectInstance instance = mbsc.getObjectInstance(threadObjName); //
	// InstanceNotFoundException
	//
	// mbsc.invoke(instance.getObjectName(), "unbind", null, null);//
	// MBeanException
	// // ReflectionException
	//
	// Thread.sleep(500); // InterruptedException
	//
	// mbsc.invoke(instance.getObjectName(), "bind", null, null);
	// // mbsc.invoke(instance.getObjectName(), "start", null,null);
	// } catch (MalformedURLException e) {
	// System.out.println("11111");
	// } catch (IOException e) {
	// System.out.println("222222");
	//
	// } catch (MalformedObjectNameException e) {
	// System.out.println("333333");
	// } catch (InstanceNotFoundException e) {
	// System.out.println("44444");
	// } catch (MBeanException e) {
	//
	// } catch (ReflectionException e) {
	//
	// } catch (InterruptedException e) {
	//
	// } finally {
	// if (null != connector) {
	// // connector.close();
	// }
	// }
	//
	// System.out.println("===============");
	//
	// }

}
