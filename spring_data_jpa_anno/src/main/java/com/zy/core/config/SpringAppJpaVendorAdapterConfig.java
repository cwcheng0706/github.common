package com.zy.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class SpringAppJpaVendorAdapterConfig {

	@Bean(name = "jpaVendorAdapter")
	public HibernateJpaVendorAdapter getHibernateJpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		
		return adapter;
	}
}
