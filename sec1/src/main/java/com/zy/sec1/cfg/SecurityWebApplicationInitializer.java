/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月20日 下午8:10:04
 */
package com.zy.sec1.cfg;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @Project: sec1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月20日 下午8:10:04
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
	public SecurityWebApplicationInitializer() {
		super(WebSecurityConfig.class);
	}
}
