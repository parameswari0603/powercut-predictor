package com.powercut.predictor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        String url = databaseUrl;

        // Fix Railway URL format
        if (url.startsWith("postgres://")) {
            url = url.replace("postgres://", "jdbc:postgresql://");
        } else if (url.startsWith("postgresql://")) {
            url = url.replace("postgresql://", "jdbc:postgresql://");
        }

        return DataSourceBuilder.create()
                .url(url)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
