package com.softserve.identitystarter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.softserve.identitystarter.model.AuthenticationImpl;
import com.softserve.identitystarter.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckingTokenService {
    private final JWSVerifier verifier;
    private final ObjectMapper objectMapper;

    public Authentication getAuthentication(String token) throws Exception {
        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        if(jwt.verify(verifier)){
            String userId = claimsSet.getSubject();
            List<Role> roles = objectMapper.readValue(((String) claimsSet.getClaim("role")),
                    new TypeReference<>() {});
            return AuthenticationImpl.builder()
                    .userId(userId)
                    .roles(roles)
                    .authenticated(true)
                    .build();
        }
        throw new Exception("Cannot parse token");
    }
}
