package com.softserve.identityservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.softserve.identityservice.configuration.properties.TokenConfigurationProperties;
import com.softserve.identityservice.model.Role;
import com.softserve.identityservice.security.AuthenticationImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenConfigurationProperties tokenConfiguration;
    private final JWSSigner signer;
    private final JWSVerifier verifier;
    private final ObjectMapper objectMapper;

    public String createToken(String email, Collection<? extends GrantedAuthority> roles) throws ServletException {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
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

    public Date getExpirationTime(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
    }

    public String getSubject(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
    }

    public JWTClaimsSet getClaims(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        return claimsSet;
    }

    public Authentication getAuthentication(String token) throws Exception {
        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        //Verify token and return 'Authentication' object.
        if(jwt.verify(verifier)){
            String email = claimsSet.getSubject();
            List<Role> roles = objectMapper.readValue(((String) claimsSet.getClaim("role")),
                    new TypeReference<>() {});
            //I use my custom Authentication implementation, but you program will be work without it.
            //Spring Security automatically create UsernamePassword authentication token (not usually).
            //For more details: https://spring.io/guides/topicals/spring-security-architecture
            return AuthenticationImpl.builder()
                    .email(email)
                    .roles(roles)
                    .authenticated(true)
                    .build();
        }
        throw new Exception("Cannot parse token");
    }
}
