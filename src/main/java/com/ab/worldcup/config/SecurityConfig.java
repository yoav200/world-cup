package com.ab.worldcup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/#/signin")
                    .loginProcessingUrl("/#/signin/authenticate")
                    .defaultSuccessUrl("/#/connect")
                    .failureUrl("/#/signin?param.error=bad_credentials")
                .and()
                    .logout()
                    .logoutUrl("/#/signout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/#/")
                .and()
                    .authorizeRequests()
                    //.antMatchers("/signin/**", "/signup/**").permitAll()
                    .anyRequest().permitAll() //.authenticated()
                .and()
                .rememberMe();
    }
}
