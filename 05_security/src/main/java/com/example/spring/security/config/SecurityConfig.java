package com.example.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/", "/home", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // H2 console for demo

                // Role-based access
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/moderator/**").hasAnyRole("ADMIN", "MODERATOR")
                .requestMatchers("/user/**").hasAnyRole("ADMIN", "MODERATOR", "USER")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/moderator/**").hasAnyRole("ADMIN", "MODERATOR")
                .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "MODERATOR", "USER")

                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            )
            // Disable CSRF for H2 console (demo purposes only)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            // Allow H2 console frames (demo purposes only)
            .headers(headers -> headers
                .frameOptions().sameOrigin()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
