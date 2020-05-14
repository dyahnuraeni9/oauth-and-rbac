package com.project.user.management.security;

import com.project.user.management.config.AuthFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/authorize").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/oauth/authorize").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/check_token").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/oauth/check_token").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/ready").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/create/password").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/otp/validation").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/user/register-sysadmin").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/password/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/test/check",
                        "/view-data/**").permitAll()
                .anyRequest()
                .access("isAuthenticated()");

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }

    public AuthFilter authenticationFilter() throws Exception {
        AuthFilter filter = new AuthFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
//        filter.setAuthenticationFailureHandler(failureHandler());
        return filter;
    }

}
