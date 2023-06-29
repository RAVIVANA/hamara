package com.nkxgen.spring.jdbc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.nkxgen.spring.jdbc")
// @PropertySource("classpath:log4j.properties")

// @PropertySource("classpath:resources/database.properties")
@EnableJpaRepositories("com.nkxgen.spring.jdbc.DaoInterfaces")
public class AppConfig {

	// @Autowired
	// Environment environment;
	//
	// private final String URL = "url";
	// private final String USER = "dbuser";
	// private final String DRIVER = "driver";
	// private final String PASSWORD = "dbpassword";
	// //
	// @Bean
	// public Logger logger() {
	// return LogManager.getLogger("log4j.properties"); // Replace with your desired logger name
	// }
	// @Bean
	// DataSource dataSource() {
	// DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	// driverManagerDataSource.setUrl(environment.getProperty(URL));
	// driverManagerDataSource.setUsername(environment.getProperty(USER));
	// driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
	// driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
	// return driverManagerDataSource;
	// }
}