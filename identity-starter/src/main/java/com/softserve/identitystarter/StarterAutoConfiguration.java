package com.softserve.identitystarter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.softserve.identitystarter.filter.AuthorizationFilter;
import com.softserve.identitystarter.service.CheckingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
public class StarterAutoConfiguration {
    private final ObjectMapper objectMapper;
    private final JWSVerifier verifier;

    //todo: Delete all useless dependencies from identity-service
    //todo: Add this starter to POM starter
    //todo: Build this project
    @ConditionalOnMissingBean(CheckingTokenService.class)
    public CheckingTokenService checkingTokenService(){
        return new CheckingTokenService(verifier, objectMapper);
    }

    @ConditionalOnMissingBean(AuthorizationFilter.class)
    public AuthorizationFilter authorizationFilter(){
        return new AuthorizationFilter(checkingTokenService());
    }
}
