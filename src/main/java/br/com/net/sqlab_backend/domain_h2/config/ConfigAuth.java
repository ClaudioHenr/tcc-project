package br.com.net.sqlab_backend.domain_h2.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import br.com.net.sqlab_backend.domain_h2.repositories.auth.AuthRepository;

// @Configuration
// @EnableJpaRepositories(
//     basePackageClasses = AuthRepository.class,
//     entityManagerFactoryRef = "authEntityManager"
// )
public class ConfigAuth {
    
	// @Bean
	// @ConfigurationProperties(prefix = "auth.datasource")
	// public DataSource authDataSource() {
	// 	return DataSourceBuilder.create().build();
	// }

	// @Bean
	// public LocalContainerEntityManagerFactoryBean authEntityManager(
	// 		EntityManagerFactoryBuilder builder,
	// 		@Qualifier("authDataSource") DataSource dataSource) {
	// 	return builder
	// 			.dataSource(dataSource)
	// 			.packages("br.com.net.sqlab_backend.domain_h2.models.auth")
	// 			.build();
	// }
}
