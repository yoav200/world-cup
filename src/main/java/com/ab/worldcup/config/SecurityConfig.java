package com.ab.worldcup.config;

import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.account.CustomUserDetailsService;
import com.ab.worldcup.account.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final AccountRepository accountRepository;

    private final ApplicationConfig applicationConfig;


    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(accountRepository, applicationConfig);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/**", "/api/**").permitAll()
                .antMatchers("/api/bet/**").hasRole(Role.USER.toString())
                .antMatchers("/api/admin/**").hasRole(Role.ADMIN.toString())
                .anyRequest().permitAll();

        http
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin().failureHandler(new MyAuthenticationFailureHandler()).loginProcessingUrl("/login")
                .successHandler(new MyAuthenticationSuccessHandler()).loginPage("/index").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/#/")
                .deleteCookies("remember-me", "JSESSIONID")
                .permitAll()
                .and()
                .rememberMe();
    }


    public static class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            // This is actually not an error, but an OK message. It is sent to avoid redirects.
            response.sendError(HttpServletResponse.SC_OK);
        }
    }

    public static class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
            response.sendError(401, "Authentication Failed: " + exception.getMessage());
        }

    }
}
