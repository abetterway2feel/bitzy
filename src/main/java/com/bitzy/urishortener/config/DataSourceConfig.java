package com.bitzy.urishortener.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DataSourceConfig {

    @Autowired
    Environment env;

    @Bean(name = "numberOfPartitions")
    public int numberOfPartitions() {
        return 100;
    }

    public DataSource extractFromEnvAndApplyMigrations(String configPrefix){
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(env.getProperty(configPrefix+".jdbc-url"));
        ds.setUsername(env.getProperty(configPrefix+".user"));
        ds.setPassword(env.getProperty(configPrefix+".password"));
        ds.setDriverClassName(env.getProperty(configPrefix+".driverClassName"));
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);

        Flyway.configure().dataSource(ds).load().migrate();
        return ds;
    }

    @Bean()
    public DataSource db1() {
       return extractFromEnvAndApplyMigrations("spring.datasource.db1");
    }

    @Bean()
    public DataSource db2() {
        return extractFromEnvAndApplyMigrations("spring.datasource.db2");
    }

    @Bean(name = "datasources")
    public DataSource[] dataSources() {
        DataSource[] datasources = { db1(), db2() };
        return datasources;
    }

}