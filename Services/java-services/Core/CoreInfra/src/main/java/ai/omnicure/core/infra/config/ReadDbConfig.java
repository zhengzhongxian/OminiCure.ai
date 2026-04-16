package ai.omnicure.core.infra.config;

import ai.omnicure.core.shared.constant.KeyConstants;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = KeyConstants.ConnectionStrings.READ_ENTITY_MANAGER_FACTORY,
        transactionManagerRef = KeyConstants.ConnectionStrings.READ_TRANSACTION_MANAGER,
        basePackages = {"ai.omnicure.**.repository.read"}
)
public class ReadDbConfig {

    @Bean(name = KeyConstants.ConnectionStrings.READ_DATA_SOURCE_PROPERTIES)
    @ConfigurationProperties(KeyConstants.ConfigurationSections.READ_DATASOURCE)
    public DataSourceProperties readDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = KeyConstants.ConnectionStrings.READ_DATA_SOURCE)
    @ConfigurationProperties(KeyConstants.ConfigurationSections.READ_HIKARI)
    public DataSource readDataSource(@Qualifier(KeyConstants.ConnectionStrings.READ_DATA_SOURCE_PROPERTIES) DataSourceProperties readDataSourceProperties) {
        HikariDataSource dataSource = readDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean(name = KeyConstants.ConnectionStrings.READ_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean readEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(KeyConstants.ConnectionStrings.READ_DATA_SOURCE) DataSource readDataSource) {

        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");

        return builder
                .dataSource(readDataSource)
                .packages("ai.omnicure.**.domain.entity")
                .persistenceUnit(KeyConstants.ConnectionStrings.READ_ENTITY_MANAGER_FACTORY)
                .properties(jpaProperties)
                .build();
    }

    @Bean(name = KeyConstants.ConnectionStrings.READ_TRANSACTION_MANAGER)
    public PlatformTransactionManager readTransactionManager(
            @Qualifier(KeyConstants.ConnectionStrings.READ_ENTITY_MANAGER_FACTORY) LocalContainerEntityManagerFactoryBean readEntityManagerFactory) {
        return new JpaTransactionManager(readEntityManagerFactory.getObject());
    }
}
