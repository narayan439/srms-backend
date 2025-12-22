package com.studentresult.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Password encoder (OK as-is)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF for APIs
            .csrf(csrf -> csrf.disable())

            // Disable Spring default login mechanisms
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

                // ✅ AUTH APIs (FIXED)
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/auth/**").permitAll()

                // ✅ Swagger
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                ).permitAll()

                // ✅ PUBLIC GET APIs
                .requestMatchers(HttpMethod.GET, "/api/classes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/subjects/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/students/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/teachers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/marks/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/rechecks/**").permitAll()

                // ✅ WRITE APIs (TEMP: open)
                .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()

                // Everything else
                .anyRequest().permitAll()
            )

            // Enable CORS
            .cors();

        return http.build();
    }
}
