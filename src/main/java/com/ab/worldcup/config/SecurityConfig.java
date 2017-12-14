package com.ab.worldcup.config;

import com.ab.worldcup.signin.AccountConnectionSignUp;
import com.ab.worldcup.signin.AccountSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private AccountConnectionSignUp accountConnectionSignUp;

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
                    //.permitAll()
                .and()
                    .logout()
                        .logoutUrl("/#/signout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/#/")
                .and()
                    .authorizeRequests()
                        .antMatchers("/login*", "/signin/**", "/signup/**").permitAll()
                        .anyRequest().permitAll() //.authenticated()
                .and()
                    .rememberMe();
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        ((InMemoryUsersConnectionRepository) usersConnectionRepository).setConnectionSignUp(accountConnectionSignUp);
        return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new AccountSignInAdapter());
    }
}
