package com.ab.worldcup.signin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "worldcup.socialauth")
public class SocialauthControllerConfig {

    private String baseCallbackUrl;

    public String successPageUrl;

    public String accessDeniedPageUrl;
}
