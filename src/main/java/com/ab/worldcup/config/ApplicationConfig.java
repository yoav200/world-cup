package com.ab.worldcup.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "worldcup")
public class ApplicationConfig {

  private String startDateTimeConfig;

  private int confirmationTimeoutMinutes;

  private String appUrl;

  private Set<String> adminEmails;

  private Set<String> allowedDomains;
  private LocalDateTime startDateTime;

  @PostConstruct
  public void init() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
    this.startDateTime = LocalDateTime.parse(startDateTimeConfig, formatter);
  }
}
