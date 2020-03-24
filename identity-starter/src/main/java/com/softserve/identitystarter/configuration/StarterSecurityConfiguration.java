package com.softserve.identitystarter.configuration;

import com.softserve.identitystarter.filter.AuthorizationFilter;
import com.softserve.identitystarter.service.CheckingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Order(2)
public class StarterSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final CheckingTokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthorizationFilter(tokenService), BasicAuthenticationFilter.class);
    }
}
