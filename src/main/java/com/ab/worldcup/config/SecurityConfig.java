package com.ab.worldcup.config;

import com.ab.worldcup.account.CustomOidcUserService;
import com.ab.worldcup.account.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;


@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final CustomOidcUserService customOidcUserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/index", "/index.html",
                "/css/**", "/js/**", "/images/**", "/app/**",
                "/favicon.ico", "/actuator/**").permitAll()
            .requestMatchers("/api/ranking/**", "/api/teams/**",
                "/api/groups/**", "/api/match/**",
                "/api/coins/bucket").permitAll()
            .requestMatchers("/api/bets/**").hasRole(Role.USER.toString())
            .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.toString())
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
        )
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .oauth2Login(oauth2 -> oauth2
            .defaultSuccessUrl("/", true)
            .userInfoEndpoint(userInfo -> userInfo
                .oidcUserService(customOidcUserService)
            )
        )
        .logout(logout -> logout
            .logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher("/logout"))
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

    return http.build();
  }
}
