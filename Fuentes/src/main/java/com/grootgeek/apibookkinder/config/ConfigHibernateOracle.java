package com.grootgeek.apibookkinder.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerOracle", transactionManagerRef = "transactionManagerOracle", basePackages = {"com.grootgeek.apibookkinder.repository"})
public class ConfigHibernateOracle {
	
	@Primary
	@Bean(name = {"dataSourceOracle"})
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSourceOracle() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = {"entityManagerOracle"})
	public LocalContainerEntityManagerFactoryBean entitityManagerOra(EntityManagerFactoryBuilder builder, @Qualifier("dataSourceOracle") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.grootgeek.apibookkinder.entities").persistenceUnit("db1").build();
	}

	@Primary
	@Bean(name = {"transactionManagerOracle"})
	public PlatformTransactionManager transaccionManager(@Qualifier("entityManagerOracle") EntityManagerFactory manager) {
		return new JpaTransactionManager(manager);
	}
}
