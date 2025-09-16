package com.venatus.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails superAdmin = User.withUsername("superadmin")
                .password(encoder.encode("password"))
                .roles("SUPERADMIN")                 // was SUPER_ADMIN
                .build();

        UserDetails recruiter  = User.withUsername("recruiter")
                .password(encoder.encode("password"))
                .roles("RECRUITER")
                .build();

        UserDetails entAdmin   = User.withUsername("eadmin")
                .password(encoder.encode("password"))
                .roles("EADMIN")                     // was ENTERPRISE_ADMIN
                .build();

        UserDetails employee   = User.withUsername("employee")
                .password(encoder.encode("password"))
                .roles("EMPLOYEE")
                .build();

        UserDetails vendor     = User.withUsername("vendor")
                .password(encoder.encode("password"))
                .roles("VENDOR")
                .build();

        return new InMemoryUserDetailsManager(superAdmin, recruiter, entAdmin, employee, vendor);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())            // pure API; fine for dev mini-server
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // dev convenience: health & swagger are open
                        .requestMatchers(
                                "/actuator/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // simple test endpoint open
                        .requestMatchers(HttpMethod.GET, "/auth/ping").permitAll()
                        // everything else needs auth
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());   // Basic Auth for now
        return http.build();
    }
}
