///**
// * @Author: zy
// * @Company: 
// * @Create Time: 2016年8月28日 下午12:01:28
// */
//package com.zy.sec1.servlet.listener;
//
//import javax.servlet.http.HttpSessionEvent;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.session.SessionInformation;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.web.session.HttpSessionEventPublisher;
//import org.springframework.stereotype.Component;
//
//import com.zy.sec1.cfg.SpringMvcConfig;
//import com.zy.sec1.entity.User;
//
///**
// * @Project: sp_sec1
// * @Author zy
// * @Company: 
// * @Create Time: 2016年8月28日 下午12:01:28
// */
///**
// * 扩展统计在线人数
// * 
// * @Project: springboot_1
// * @Author zy
// * @Company:
// * @Create Time: 2016年8月27日 下午9:08:30
// */
//@Component
//public class CustomerHttpSessionEventPublisher extends HttpSessionEventPublisher {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHttpSessionEventPublisher.class);
//	
//	@Autowired
//	private SessionRegistry sessionRegistry;
//			
//	public void sessionCreated(HttpSessionEvent event) {
//		// 将用户加入到在线用户列表中
//		saveOrDeleteOnlineUser(event, Type.SAVE);
//		super.sessionCreated(event);
//	}
//
//	public void sessionDestroyed(HttpSessionEvent event) {
//		// 将用户从在线用户列表中移除
//		saveOrDeleteOnlineUser(event, Type.DELETE);
//		super.sessionDestroyed(event);
//	}
//
//	public void saveOrDeleteOnlineUser(HttpSessionEvent event, Type type) {
//		
////		switch (type) {
////		case SAVE:
////			LOGGER.info("将用户sessionId【" + event.getSession().getId() + "】加入到在线用户列表中");
////			SpringMvcConfig.OnlineUserList.add(event.getSession().getId());// List<String>
////			break;
////		case DELETE:
////			LOGGER.info("将用户sessionId【" + event.getSession().getId() + "】从在线用户列表中移除");
////			SpringMvcConfig.OnlineUserList.remove(event.getSession().getId());
////			break;
////		}
//		
////		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
//		
//		SessionInformation sessionInfo = sessionRegistry.getSessionInformation(event.getSession().getId());
//		Object principal = sessionInfo.getPrincipal();
//		
//		if (principal instanceof User) {
//			User user = (User) principal;
//
//			switch (type) {
//			case SAVE:
//				LOGGER.info("将用户【" + user.getUsername() + "】加入到在线用户列表中");
//				SpringMvcConfig.OnlineUserList.add(user.getUsername() + sessionInfo.getSessionId());// List<String>
//				break;
//			case DELETE:
//				LOGGER.info("将用户【" + user.getUsername() + "】从在线用户列表中移除");
//				SpringMvcConfig.OnlineUserList.remove(user.getUsername() + sessionInfo.getSessionId());
//				break;
//			}
//		}
//	}
//
//	/**
//	 * 定义一个简单的内部枚举
//	 */
//	private static enum Type {
//		SAVE, DELETE;
//	}
//}
