package com.lhind.annualleave.http.security;

import com.lhind.annualleave.http.exceptions.UserNotAuthenticatedException;
import com.lhind.annualleave.services.InvalidTokenService;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(AuthorizationFilter.class);
    public final InvalidTokenService invalidTokenService;

    public AuthorizationFilter(InvalidTokenService invalidTokenService) {
        this.invalidTokenService = invalidTokenService;
    }

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {

            if (checkJWTToken(request)) {
                Claims claims = validateToken(request);
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException
                | MalformedJwtException | UserNotAuthenticatedException e) {
            LOGGER.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private boolean checkJWTToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(JwtTokenUtils.HEADER);
        if (authenticationHeader == null) {
            return false;
        }

        String jwtToken = authenticationHeader.replace(JwtTokenUtils.PREFIX, "");
        if (invalidTokenService.isTokenStoredAsInvalid(jwtToken)) {
            throw new UserNotAuthenticatedException();
        }
        return authenticationHeader.startsWith(JwtTokenUtils.PREFIX);
    }

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(JwtTokenUtils.HEADER)
                .replace(JwtTokenUtils.PREFIX, "");
        return Jwts.parser()
                .setSigningKey(JwtTokenUtils.SECRET.getBytes())
                .parseClaimsJws(jwtToken).getBody();
    }

    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
