package com.softserve.identityservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.softserve.identityservice.configuration.properties.TokenConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenConfigurationProperties tokenConfiguration;
    private final JWSSigner signer;
    private final ObjectMapper objectMapper;

    public String createToken(String userId, Collection<? extends GrantedAuthority> roles) throws ServletException {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId)
                    .issuer(tokenConfiguration.getHost())
                    .claim("role", objectMapper.writeValueAsString(roles))
                    .expirationTime(Date.from(Instant.now().plus(tokenConfiguration.getExpiration(),
                            ChronoUnit.MINUTES)))
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
            signedJWT.sign(signer);
            return "Bearer " + signedJWT.serialize();
        }catch (JOSEException | JsonProcessingException e){
            throw new ServletException(e);
        }
    }
}
