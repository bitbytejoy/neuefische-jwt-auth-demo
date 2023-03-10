package de.neuefische.neuefischejwttokendemo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("jwt.secret")
    private String jwtSecret;

    private final AppUserService appUserService;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http
            .csrf().disable()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .addFilterBefore(new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                    String jwtToken = Optional.ofNullable(
                        request.getHeader("Authorization")
                    ).orElse("")
                    .replaceFirst("Bearer ", "");

                    SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                            new JwtAuthentication(
                                jwtToken,
                                appUserService,
                                jwtSecret
                            )
                    );

                    filterChain.doFilter(request, response);
                }
            }, UsernamePasswordAuthenticationFilter.class)

            .authorizeHttpRequests()
            .antMatchers(
                HttpMethod.POST,
                "/api/app-users",
                "/api/login"
            ).permitAll()
            .anyRequest()
            .authenticated()

            .and()
            .build();
    }
}
