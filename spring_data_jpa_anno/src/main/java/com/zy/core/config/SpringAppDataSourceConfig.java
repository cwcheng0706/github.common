package com.zy.core.config;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@Import(value = {
		SpringAppJpaVendorAdapterConfig.class
})
public class SpringAppDataSourceConfig {
	
	private final static Logger logger = Logger.getLogger(SpringAppDataSourceConfig.class);

	@Value("${driverClass}") 
	private String driverClass;
	
	@Value("${jdbcUrl}")
	private String jdbcUrl;
	
	@Value("${user}")
	private String user;
	
	@Value("${password}")
	private String password;
	
	@Value("${acquireIncrement}")
	private String acquireIncrement;
	
	@Value("${initialPoolSize}")
	private String initialPoolSize;
	
	@Value("${maxIdleTime}")
	private String maxIdleTime;
	
	@Value("${maxPoolSize}")
	private String maxPoolSize;
	
	@Value("${minPoolSize}")
	private String minPoolSize;
	
	@Value("${acquireRetryDelay}")
	private String acquireRetryDelay;
	
	@Value("${acquireRetryAttempts}")
	private String acquireRetryAttempts;
	
	@Value("${breakAfterAcquireFailure}")
	private String breakAfterAcquireFailure;

	@Bean(name = "mySqlDataSource")
	public DataSource dataSource() {
		ComboPooledDataSource dataSource = null;
		try{
			dataSource = new ComboPooledDataSource();
			
			dataSource.setDriverClass(driverClass);
			dataSource.setUser(user);
			dataSource.setJdbcUrl(jdbcUrl);
			dataSource.setPassword(password);
	
			if(null != acquireIncrement && !"".equals(acquireIncrement)) {
				dataSource.setAcquireIncrement(Integer.valueOf(acquireIncrement.trim()));
			}
			if(null != initialPoolSize && !"".equals(initialPoolSize)) {
				dataSource.setInitialPoolSize(Integer.valueOf(initialPoolSize.trim()));
			}
			if(null != maxIdleTime && !"".equals(maxIdleTime)) {
				dataSource.setMaxIdleTime(Integer.valueOf(maxIdleTime.trim()));
			}
			if(null != maxPoolSize && !"".equals(maxPoolSize)) {
				dataSource.setMaxPoolSize(Integer.valueOf(maxPoolSize.trim()));
			}
			if(null != minPoolSize && !"".equals(minPoolSize)) {
				dataSource.setMinPoolSize(Integer.valueOf(minPoolSize.trim()));
			}
			if(null != acquireRetryDelay && !"".equals(acquireRetryDelay)) {
				dataSource.setAcquireRetryDelay(Integer.valueOf(acquireRetryDelay.trim()));
			}
			if(null != acquireRetryAttempts && !"".equals(acquireRetryAttempts)) {
				dataSource.setAcquireRetryAttempts(Integer.valueOf(acquireRetryAttempts.trim()));
			}
			if(null != breakAfterAcquireFailure && !"".equals(breakAfterAcquireFailure)) {
				dataSource.setBreakAfterAcquireFailure(Boolean.parseBoolean(breakAfterAcquireFailure.trim()));
			}
			
		}catch(Exception e) {
			logger.error("初始化DataSourc异常【" + e + "】");
		}
		return dataSource;
	}

}
