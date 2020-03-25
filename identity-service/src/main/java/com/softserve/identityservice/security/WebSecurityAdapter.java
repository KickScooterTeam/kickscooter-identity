package com.softserve.identityservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.identityservice.service.TokenService;
import com.softserve.identitystarter.filter.AuthorizationFilter;
import com.softserve.identitystarter.service.CheckingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Order(1)
@RequiredArgsConstructor
public class WebSecurityAdapter extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_PAGE_URL = "/sign-in";

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CheckingTokenService checkingTokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/sign-up", "/activate/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(new AuthorizationFilter(checkingTokenService), BasicAuthenticationFilter.class)
                .addFilterBefore(new AuthenticationFilter(LOGIN_PAGE_URL, tokenService, objectMapper,
                        authenticationManager()), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
