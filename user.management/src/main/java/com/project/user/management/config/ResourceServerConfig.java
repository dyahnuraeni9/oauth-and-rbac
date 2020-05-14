package com.project.user.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "resource-id";
    public static final String CLIENT_ID = "client-id";
    public static final String CLIENT_SECRET = "client-secret";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
                    "/v2/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/test/check",
                        "/view-data/**"
                        ).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .access("isAuthenticated()")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}
