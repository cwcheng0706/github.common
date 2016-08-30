/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月27日 下午1:29:09
 */
package com.zy.sec1.controll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月27日 下午1:29:09
 */
@Controller
@RequestMapping("/redis")
public class RedisController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@RequestMapping("/transcation")
	public void testRedisTranscation() {

		ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
		ops.set("test", "勇");

		// execute a transaction
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
			public List<Object> execute(RedisOperations operations) throws DataAccessException {
				operations.multi();

				operations.opsForSet().add("key", "周冬");

				// This will contain the results of all ops in the transaction
				return operations.exec();
			}

		});
		System.out.println("Number of items added to set: " + txResults.get(0));
	}
}
