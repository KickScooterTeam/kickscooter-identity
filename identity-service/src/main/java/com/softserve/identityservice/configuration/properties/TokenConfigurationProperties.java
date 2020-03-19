package com.softserve.identityservice.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("token")
public class TokenConfigurationProperties {
    private String host;
    private Long expiration;
}
