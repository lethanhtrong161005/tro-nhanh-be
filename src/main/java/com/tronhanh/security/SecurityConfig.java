package com.tronhanh.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration bean establishing stateless filter chain and endpoint permits.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final TraceIdFilter traceIdFilter;

  /**
   * SecurityConfig constructor.
   *
   * @param traceIdFilter MDC trace ID filter
   */
  public SecurityConfig(TraceIdFilter traceIdFilter) {
    this.traceIdFilter = traceIdFilter;
  }

  /**
   * Configures SecurityFilterChain with stateless session policy and TraceIdFilter.
   *
   * @param http HttpSecurity configuration object
   * @return SecurityFilterChain instance
   * @throws Exception if security configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> {})
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/actuator/**",
                        "/api/v1/health")
                    .permitAll()
                    .anyRequest()
                    .permitAll())
        .addFilterBefore(traceIdFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
