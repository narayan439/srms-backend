package com.studentresult.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * Configure BCrypt password encoder
     * This bean can be injected anywhere in the application
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configure HTTP security
     * Disable default form login and basic auth
     * Allow all requests to custom auth endpoints
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()  // Allow all auth endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()  // Allow Swagger
                .requestMatchers("GET", "/classes/**").permitAll()  // Allow GET requests to view classes
                .requestMatchers("GET", "/subjects/**").permitAll()  // Allow GET requests to view subjects
                .requestMatchers("GET", "/students/**").permitAll()  // Allow GET requests to view students
                .requestMatchers("GET", "/teachers/**").permitAll()  // Allow GET requests to view teachers
                .requestMatchers("GET", "/marks/**").permitAll()  // Allow GET requests to view marks
                .requestMatchers("GET", "/rechecks/**").permitAll()  // Allow GET requests to view rechecks
                .requestMatchers("POST", "/subjects/**").permitAll()  // Allow POST to add subjects
                .requestMatchers("DELETE", "/subjects/**").permitAll()  // Allow DELETE for subjects
                .requestMatchers("PUT", "/subjects/**").permitAll()  // Allow PUT to update subjects
                .requestMatchers("POST", "/teachers/**").permitAll()  // Allow POST to add teachers
                .requestMatchers("DELETE", "/teachers/**").permitAll()  // Allow DELETE for teachers
                .requestMatchers("PUT", "/teachers/**").permitAll()  // Allow PUT to update teachers
                .requestMatchers("POST", "/classes/**").permitAll()  // Allow POST to add classes
                .requestMatchers("DELETE", "/classes/**").permitAll()  // Allow DELETE for classes
                .requestMatchers("PUT", "/classes/**").permitAll()  // Allow PUT to update classes
                .requestMatchers("POST", "/rechecks/**").permitAll()  // Allow POST to create recheck requests
                .requestMatchers("DELETE", "/rechecks/**").permitAll()  // Allow DELETE for rechecks
                .requestMatchers("PUT", "/rechecks/**").permitAll()  // Allow PUT to update rechecks
                .anyRequest().authenticated()  // All other requests require authentication
            .and()
            .formLogin().disable()  // Disable form login
            .httpBasic().disable()  // Disable basic auth
            .cors();
        
        return http.build();
    }
}
