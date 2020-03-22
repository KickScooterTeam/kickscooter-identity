package com.softserve.identityservice.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.identityservice.model.Role;
import com.softserve.identityservice.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private TokenService tokenService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        //Get token from header
        String token = req.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        //Make 'Authentication' object and put it into SecurityContextHolder.
        try {
            SecurityContextHolder.getContext().setAuthentication((tokenService.getAuthentication(token.substring(7))));
        } catch (Exception e) {
            logger.debug("In doFilterInternal(): ", e);
        } finally {
            chain.doFilter(req, res);
        }
    }
}
