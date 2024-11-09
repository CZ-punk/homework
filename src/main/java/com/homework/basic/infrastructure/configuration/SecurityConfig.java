package com.homework.basic.infrastructure.configuration;

import com.homework.basic.application.jwt.JwtAuthenticationEntryPoint;
import com.homework.basic.presentation.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthorizationFilter authorizationFilter;
  private final JwtAuthenticationEntryPoint entryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers("/api/user/signup")
                    .permitAll()
                    .requestMatchers("/api/user/sign")
                    .permitAll()
                    .anyRequest()
                    .authenticated());

    http.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));
    http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
