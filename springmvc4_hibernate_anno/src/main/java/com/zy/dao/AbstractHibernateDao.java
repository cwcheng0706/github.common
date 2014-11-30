/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 下午1:48:31
 */
package com.zy.dao;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company: 
 * @Create Time: 2014年9月13日 下午1:48:31
 */
@Repository
public class AbstractHibernateDao extends HibernateDaoSupport {
	
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	protected JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired  
    public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
      super.setSessionFactory(sessionFactory);   
    }
	
}
