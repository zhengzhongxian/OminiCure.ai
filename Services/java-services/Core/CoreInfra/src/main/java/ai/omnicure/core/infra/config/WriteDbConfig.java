package ai.omnicure.core.infra.config;

import ai.omnicure.core.shared.constant.KeyConstants;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = KeyConstants.ConnectionStrings.WRITE_ENTITY_MANAGER_FACTORY,
        transactionManagerRef = KeyConstants.ConnectionStrings.WRITE_TRANSACTION_MANAGER,
        basePackages = {"ai.omnicure"}
)
public class WriteDbConfig {

    @Primary
    @Bean(name = KeyConstants.ConnectionStrings.WRITE_DATA_SOURCE_PROPERTIES)
    @ConfigurationProperties(KeyConstants.ConfigurationSections.WRITE_DATASOURCE)
    public DataSourceProperties writeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = KeyConstants.ConnectionStrings.WRITE_DATA_SOURCE)
    @ConfigurationProperties(KeyConstants.ConfigurationSections.WRITE_HIKARI)
    public DataSource writeDataSource(@Qualifier(KeyConstants.ConnectionStrings.WRITE_DATA_SOURCE_PROPERTIES) DataSourceProperties writeDataSourceProperties) {
        return writeDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = KeyConstants.ConnectionStrings.WRITE_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean writeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(KeyConstants.ConnectionStrings.WRITE_DATA_SOURCE) DataSource writeDataSource) {
            
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(writeDataSource)
                .packages("ai.omnicure")
                .persistenceUnit(KeyConstants.ConnectionStrings.WRITE_ENTITY_MANAGER_FACTORY)
                .properties(jpaProperties)
                .build();
    }

    @Primary
    @Bean(name = KeyConstants.ConnectionStrings.WRITE_TRANSACTION_MANAGER)
    public PlatformTransactionManager writeTransactionManager(
            @Qualifier(KeyConstants.ConnectionStrings.WRITE_ENTITY_MANAGER_FACTORY) LocalContainerEntityManagerFactoryBean writeEntityManagerFactory) {
        return new JpaTransactionManager(writeEntityManagerFactory.getObject());
    }
}
