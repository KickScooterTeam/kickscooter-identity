package com.softserve.identityservice.configuration;

import com.softserve.identityservice.configuration.properties.KeyConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        byte[] bytes = Files.readAllBytes(Paths.get(keyConfiguration.getPrivateKeyPath().getURI()));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Bean
    public RSAPublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(keyConfiguration.getPublicKeyPath().getURI()));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }
}
