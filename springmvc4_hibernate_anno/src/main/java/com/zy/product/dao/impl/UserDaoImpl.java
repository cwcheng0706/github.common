/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:08:18
 */
package com.zy.product.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.zy.core.dao.AbstractHibernateDao;
import com.zy.entity.User;
import com.zy.product.dao.UserDao;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午10:08:18
 */
@Repository
public class UserDaoImpl extends AbstractHibernateDao implements UserDao {

	public void save(User user) {

		//新增1
		this.getHibernateTemplate().save(user);

		// Integer.parseInt("sss");

		//新增2
//		this.jdbcTemplate.execute("INSERT INTO t_product(id,NAME,createDate) VALUES('11111','朱勇',NOW())");

		//查询1
//		List<Map<String, Object>> ret = this.jdbcTemplate.queryForList("select * from t_user");
//
//		System.out.println("==" + ret.size());
		

//		String sql = "INSERT INTO t_product(id,NAME,createDate) VALUES('222222',':name',NOW())";
//		SqlParameterSource namedParameters = new MapSqlParameterSource("first_name", "朱勇");
		//新增3
//		this.namedParameterJdbcTemplate.update(sql, namedParameters);
		
		//查询2
//		SqlParameterSource namedParameters = new MapSqlParameterSource("name","朱勇");
//		List<Map<String, Object>> list = this.namedParameterJdbcTemplate.queryForList("select * from t_product t where t.name like :name", namedParameters);

//		System.out.println("==" + list.size());

	}

	@SuppressWarnings("unchecked")
	@Override
	public User getUserByMail(String mail) {
		List<User> list = (List<User>)getHibernateTemplate().find("select u from User u where u.mail = ?", mail);
		if(null != list && 0 < list.size()) {
			return list.get(0);
		}
		return null;
	}

}
