package com.enceladus.delivery.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // OpenAPI docs access
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Health endpoints - both direct and proxied
                        .requestMatchers(
                                "/howru/**",
                                "/backend/howru/**",
                                "/dashboard/howru/**"
                        ).permitAll()
                        .requestMatchers(
                                "/version",
                                "/backend/version")
                        .permitAll()

                        // Everything else secured
                        .anyRequest().permitAll() // FIXME ting tong
                )

                // Disable CSRF on specific paths (stateless)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/howru/**",
                                "/backend/howru/**",
                                "/dashboard/howru/**",
                                "/**"
                        )
                )

                // Avoid JSESSIONID cookies for health and REST
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Optional: use HTTP Basic or something else
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

