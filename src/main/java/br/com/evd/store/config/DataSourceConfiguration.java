package br.com.evd.store.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DataSourceConfiguration {

//	@Bean(name = "customDataSource")
//    @ConfigurationProperties("spring.datasource")
//	public DataSource customDataSource() {
//		return DataSourceBuilder.create().build();
//	}
	
}
