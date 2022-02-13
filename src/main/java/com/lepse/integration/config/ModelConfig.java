package com.lepse.integration.config;

import com.lepse.integrations.log.LogsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@PropertySource({"classpath:application.properties"})
public class ModelConfig {

    final Environment environment;
    public static String urlToUOM;

    @Autowired
    public ModelConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource modelDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.driver-class-name")));
        urlToUOM = environment.getProperty("urlToUOM");

        return dataSource;
    }

    @Bean
    public JdbcTemplate modelJdbcTemplate() {
        return new JdbcTemplate(modelDataSource());
    }

    @Bean
    public LogsDAO modelLogsDAO() {
        final String logsRepositoryUrl = environment.getProperty("logs.repository.url");
        return new LogsDAO(logsRepositoryUrl);
    }
}
