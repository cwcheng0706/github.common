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

import com.zy.dao.AbstractHibernateDao;
import com.zy.entity.Product;
import com.zy.product.dao.ProductDao;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午10:08:18
 */
@Repository
public class ProductDaoImpl extends AbstractHibernateDao implements ProductDao {

	public void save(Product product) {

		//新增1
		this.getHibernateTemplate().save(product);

		// Integer.parseInt("sss");

		//新增2
		this.jdbcTemplate.execute("INSERT INTO t_product(id,NAME,createDate) VALUES('11111','朱勇',NOW())");

		//查询1
		List<Map<String, Object>> ret = this.jdbcTemplate.queryForList("select * from t_product");

		System.out.println("==" + ret.size());
		

		String sql = "INSERT INTO t_product(id,NAME,createDate) VALUES('222222',':name',NOW())";
		SqlParameterSource namedParameters = new MapSqlParameterSource("first_name", "朱勇");
		//新增3
		this.namedParameterJdbcTemplate.update(sql, namedParameters);
		
		//查询2
		namedParameters = new MapSqlParameterSource("name","朱勇");
		List<Map<String, Object>> list = this.namedParameterJdbcTemplate.queryForList("select * from t_product t where t.name = :name", namedParameters);

		System.out.println("==" + list.size());

	}

}
