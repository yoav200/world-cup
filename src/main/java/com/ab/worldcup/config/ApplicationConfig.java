package com.ab.worldcup.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "worldcup")
public class ApplicationConfig {
    private String startDateTimeConfig;

    private int confirmationTimeoutMinutes;

    private String appUrl;

    private Set<String> adminEmails;
    private LocalDateTime startDateTime;

    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        this.startDateTime = LocalDateTime.parse(startDateTimeConfig, formatter);
    }
}
