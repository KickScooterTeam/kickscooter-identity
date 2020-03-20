package com.softserve.identityservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.identityservice.model.ErrorResponse;
import com.softserve.identityservice.model.SignInDto;
import com.softserve.identityservice.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private ObjectMapper objectMapper;

    protected AuthenticationFilter(String defaultFilterProcessesUrl, TokenService tokenService,
                                   ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        try{
            SignInDto credentials = objectMapper.readValue(request.getInputStream(), SignInDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>()
                    )
            );
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String accessToken =
                tokenService.createToken(((User) authResult.getPrincipal()).getUsername(), authResult.getAuthorities());
        response.addHeader("Authorization", accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .time(LocalDateTime.now())
                .error(failed.getMessage()).build();
        writer.println(objectMapper.writeValueAsString(errorResponse));
    }
}
