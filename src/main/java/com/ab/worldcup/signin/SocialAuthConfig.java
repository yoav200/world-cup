package com.ab.worldcup.signin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("app.socialauth")
public class SocialAuthConfig {

    private String baseCallbackUrl;

    public String successPageUrl;

    public String accessDeniedPageUrl;

    public String getBaseCallbackUrl() {
        return baseCallbackUrl;
    }

    public String getSuccessPageUrl() {
        return successPageUrl;
    }

    public String getAccessDeniedPageUrl() {
        return accessDeniedPageUrl;
    }
}
