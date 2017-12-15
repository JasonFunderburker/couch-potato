package com.jasonfunderburker.couchpotato;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created on 28.02.2017
 *
 * @author JasonFunderburker
 */
@Configuration
public class DataSourceConfiguration {
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.driverClassName}")
    private String dataSourceDriverClass;

    @Bean
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(dataSourceDriverClass);
        dataSource.setUrl(dataSourceUrl);
        return dataSource;
    }
}
