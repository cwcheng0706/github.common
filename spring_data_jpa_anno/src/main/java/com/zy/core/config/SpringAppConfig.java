package com.zy.core.config;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(
		basePackages = "com.zy", 
		excludeFilters = {
				@ComponentScan.Filter(
						type = FilterType.ANNOTATION, 
						value = { org.springframework.stereotype.Controller.class }
						) 
				}
		)
@Import(value = {
		SpringAppDataSourceConfig.class,
		SpringAppEntityManagerFactoryConfig.class
		})
@ImportResource("classpath:applicationContext-properties.xml")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "com.zy.product.repository")
public class SpringAppConfig {
	
	@Resource(name = "entityManagerFactory")
	private EntityManagerFactory entityManagerFactory; 

	@Bean(name = "transactionManager")
	public JpaTransactionManager getJpaTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
		return jpaTransactionManager;
	}
	
	@Bean
	public PersistenceAnnotationBeanPostProcessor getPersistenceAnnotationBeanPostProcessor(){
		PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor = new PersistenceAnnotationBeanPostProcessor();
		return persistenceAnnotationBeanPostProcessor;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor getPersistenceExceptionTranslationPostProcessor() {
		PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor = new PersistenceExceptionTranslationPostProcessor();
		return persistenceExceptionTranslationPostProcessor;
	}
	
}
