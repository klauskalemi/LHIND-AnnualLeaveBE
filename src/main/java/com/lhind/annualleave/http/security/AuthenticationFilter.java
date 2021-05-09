package com.lhind.annualleave.http.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.services.InvalidTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;

    private final InvalidTokenService invalidTokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                InvalidTokenService invalidTokenService) {
        this.authenticationManager = authenticationManager;
        this.invalidTokenService = invalidTokenService;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginCredentials loginCredentials = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginCredentials.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginCredentials.getUsername(), loginCredentials.getPassword()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Error authenticating user: " + e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        invalidTokenService.deleteTokensOfUser(principal);

        List<String> authorities = authentication
                .getAuthorities()
                .stream()
                .map((Function<GrantedAuthority, String>) GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + JwtTokenUtils.TOKEN_EXPIRATION_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS512, JwtTokenUtils.SECRET.getBytes())
                .compact();

        response.addHeader(JwtTokenUtils.HEADER, JwtTokenUtils.PREFIX + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter()
                    .write("{\"" + JwtTokenUtils.HEADER + "\":\"" + JwtTokenUtils.PREFIX + token + "\"}");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}