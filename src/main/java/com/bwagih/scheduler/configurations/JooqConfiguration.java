package com.bwagih.scheduler.configurations;

import com.bwagih.scheduler.utils.Defines;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT5M")
@Profile(Defines.SPRING_PROFILES_JOOQ)
public class JooqConfiguration {
    @Autowired
    Environment environment;

    @Bean(name = {"dataSource1"})
    DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(1800000);
        dataSource.setNumTestsPerEvictionRun(3);
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        dataSource.setMaxActive(Integer.parseInt(environment.getProperty("dataSource.max.active", "8"))); //8
        dataSource.setMinIdle(Integer.parseInt(environment.getProperty("dataSource.min.idle", "0")));//0
        dataSource.setMaxIdle(Integer.parseInt(environment.getProperty("dataSource.max.idle", "8")));//8
        dataSource.setInitialSize(Integer.parseInt(environment.getProperty("dataSource.initial.size", "0")));//0
        dataSource.setValidationQuery(environment.getProperty("spring.datasource.validationQuery", "SELECT 1 FROM DUAL"));

        return dataSource;
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SpringTransactionProvider transactionProvider() {
        return new SpringTransactionProvider(transactionManager());
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    public ExceptionTranslator exceptionTransformer() {
        return new ExceptionTranslator();
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(exceptionTransformer()));
        jooqConfiguration.set(transactionProvider());
        jooqConfiguration.set(SQLDialect.ORACLE12C);
        return jooqConfiguration;
    }


}
