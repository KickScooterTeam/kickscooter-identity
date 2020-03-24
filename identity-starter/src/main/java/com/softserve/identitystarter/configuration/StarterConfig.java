package com.softserve.identitystarter.configuration;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class StarterConfig {
    private final String PATH = "key/public_key.der";

    @Bean
    public RSAPublicKey getPublicKey() throws Exception {
        try(InputStream inputStream = new ClassPathResource(PATH).getInputStream()) {
            byte[] keyBytes = inputStream.readAllBytes();
            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        }
    }

    @Bean
    public JWSVerifier verifier() throws Exception {
        return new RSASSAVerifier(getPublicKey());
    }
}
