/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月27日 下午8:06:01
 */
package com.zy.sec1.cfg.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月27日 下午8:06:01
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=60,redisNamespace="zy")
public class RedisSessionConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionConfig.class);

//	@Bean
//	public HttpSessionStrategy httpSessionStrategy() {
//		return new HeaderHttpSessionStrategy();
//	}
}
