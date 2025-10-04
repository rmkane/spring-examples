package org.foo.security.config;

import org.foo.security.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig implements WebMvcConfigurer {

    // Role constants
    private static final String ROLE_MODERATOR = Role.MODERATOR.name();
    private static final String ROLE_ADMIN = Role.ADMIN.name();
    private static final String ROLE_USER = Role.USER.name();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/", "/home", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // H2 console for demo
                .requestMatchers("/api/public").permitAll() // Public API endpoint

                // Role-based access
                .requestMatchers("/admin/**").hasRole(ROLE_ADMIN)
                .requestMatchers("/moderator/**").hasAnyRole(ROLE_ADMIN, ROLE_MODERATOR)
                .requestMatchers("/user/**").hasAnyRole(ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER)
                .requestMatchers("/api/admin/**").hasRole(ROLE_ADMIN)
                .requestMatchers("/api/moderator/**").hasAnyRole(ROLE_ADMIN, ROLE_MODERATOR)
                .requestMatchers("/api/user/**").hasAnyRole(ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER)

                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
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
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
