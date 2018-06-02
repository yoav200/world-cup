package com.ab.worldcup.config;

import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ApplicationConfig {

    @Value("${worldcup.start-date-time}")
    private String startDateTimeConfig;

    @Value("${worldcup.admin-emails}")
    private String administrators;


    private LocalDateTime startDateTime;

    private ImmutableSet<String> adminEmails;


    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        this.startDateTime = LocalDateTime.parse(startDateTimeConfig, formatter);
        this.adminEmails = ImmutableSet.copyOf(administrators.split(","));
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public ImmutableSet<String> getAdminEmails() {
        return adminEmails;
    }
}
