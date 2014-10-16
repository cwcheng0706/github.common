package com.zy.core.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@Import(value = {
		SpringAppJpaVendorAdapterConfig.class,
		SpringAppDataSourceConfig.class
})
public class SpringAppEntityManagerFactoryConfig {

	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;
	
	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;
	
	@Value("${hibernate.format_sql}")
	private String hibernateFormatSql;
	
	@Value("${hibernate.dialect}")
	private String hibernateDialect;
	
	@Resource(name = "mySqlDataSource")
	private DataSource dataSource;
	
	@Resource(name = "jpaVendorAdapter")
	private HibernateJpaVendorAdapter jpaVendorAdapter;
	
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean(){
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
		jpaProperties.put("hibernate.show_sql", hibernateShowSql);
		jpaProperties.put("hibernate.format_sql", hibernateFormatSql);
		jpaProperties.put("hibernate.dialect", hibernateDialect);
		
		localContainerEntityManagerFactoryBean.setDataSource(dataSource);
		localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties );
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		localContainerEntityManagerFactoryBean.setPackagesToScan("com.zy.entity");
		return localContainerEntityManagerFactoryBean;
	}
}
