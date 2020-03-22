package com.softserve.identitystarter.configuration;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class StarterConfig {
    @Value("${key.public}")
    private String path;

    @Bean
    public RSAPublicKey getPublicKey() throws Exception {
        Resource resource = new ClassPathResource(path);
        byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    @Bean
    public JWSVerifier verifier() throws Exception {
        return new RSASSAVerifier(getPublicKey());
    }
}
