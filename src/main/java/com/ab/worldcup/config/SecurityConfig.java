package com.ab.worldcup.config;

import com.ab.worldcup.signin.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
            .headers().frameOptions().disable()
            //.and()
            //    .anonymous() //allow anonymous access
            .and()
                .authorizeRequests()
                .antMatchers("/**", "/api/**").permitAll()
                .antMatchers("/api/bet/**").hasRole(Role.USER.toString())
                .antMatchers("/api/admin/**").hasRole(Role.ADMIN.toString())
            .and()
                .logout()
                .logoutUrl("/signout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/#/")
            .and()
            .rememberMe();
    }
}
