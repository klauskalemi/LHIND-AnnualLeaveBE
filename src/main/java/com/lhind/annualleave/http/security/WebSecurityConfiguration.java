package com.lhind.annualleave.http.security;

import com.lhind.annualleave.services.InvalidTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final InvalidTokenService invalidTokenService;

    private static final String[] WHITELISTED_PATHS = {
            "/v2/api-docs",
            "/webjars/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger/**",
    };

    @Autowired
    public WebSecurityConfiguration(BCryptPasswordEncoder passwordEncoder,
                                    @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                                    InvalidTokenService invalidTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.invalidTokenService = invalidTokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(new AuthenticationFilter(authenticationManager(), invalidTokenService))
                    .addFilterAfter(new AuthorizationFilter(invalidTokenService), AuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .antMatchers(WHITELISTED_PATHS).permitAll()
                    .antMatchers("/users/**").permitAll()
                    .antMatchers("/leave_requests/**").hasAnyRole("ADMIN")
                    .antMatchers("/roles/**")
                        .hasAnyRole("ADMIN", "SIMPLE", "FINANCE", "SUPERVISOR")
                    .antMatchers("/auth/**")
                        .hasAnyRole("ADMIN", "SIMPLE", "FINANCE", "SUPERVISOR")
                    .anyRequest()
                    .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
