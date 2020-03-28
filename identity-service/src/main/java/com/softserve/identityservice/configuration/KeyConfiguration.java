package com.softserve.identityservice.configuration;

import com.softserve.identityservice.configuration.properties.KeyConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
@RequiredArgsConstructor
public class KeyConfiguration {
    private final KeyConfigurationProperties keyConfiguration;

    @Bean
    public PrivateKey getPrivateKey() throws Exception {
        try(InputStream inputStream = new ClassPathResource(keyConfiguration.getPrivateKeyPath()).getInputStream()) {
            byte[] keyBytes = inputStream.readAllBytes();
            PKCS8EncodedKeySpec spec =
                    new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
    }
}
