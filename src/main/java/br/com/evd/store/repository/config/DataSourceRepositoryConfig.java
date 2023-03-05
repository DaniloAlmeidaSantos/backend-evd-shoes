package br.com.evd.store.repository.config;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceRepositoryConfig {

	@Value("${market.database.driver}")
	private static String driver;

	@Value("${market.database.url}")
	private static String url;

	@Value("${market.database.username}")
	private static String username;

	@Value("${market.database.password}")
	private static String password;
	
	@Value("${market.database.minIdle}")
	private static String minIdle;
	
	@Value("${market.database.maxIdleSize}")
	private static String maxIdleSize;

	private static Connection connection = null;

	protected static Connection openConnection() throws SQLException {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		config.setDriverClassName(driver);
		config.addDataSourceProperty("minimumIdle", minIdle);
		config.addDataSourceProperty("maximumPoolSize", maxIdleSize);
		
		HikariDataSource dataSource = new HikariDataSource(config);
		connection = dataSource.getConnection();
		connection.beginRequest();
		return connection;
	}
	
	protected static void closeConnection() throws SQLException {
		connection.endRequest();
		connection.close();
	}
}
