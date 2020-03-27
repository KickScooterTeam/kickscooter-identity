package com.softserve.identitystarter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.softserve.identitystarter.filter.AuthorizationFilter;
import com.softserve.identitystarter.service.CheckingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({AuthorizationFilter.class,CheckingTokenService.class})
@RequiredArgsConstructor
public class StarterAutoConfiguration {
    private final ObjectMapper objectMapper;
    private final JWSVerifier verifier;

    @Bean
    @ConditionalOnMissingBean(CheckingTokenService.class)
    public CheckingTokenService checkingTokenService(){
        return new CheckingTokenService(verifier, objectMapper);
    }
}
