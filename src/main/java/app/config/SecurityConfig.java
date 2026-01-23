package app.config;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS handled by CorsConfig
        http.cors(cors -> {});

        // Disable CSRF (API-only backend)
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                // React static build
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/assets/**",
                        "/favicon.svg",
                        "/static/**"
                ).permitAll()

                // Public API endpoints
                .requestMatchers("/api/auth/**").permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
        );

        // Disable form login + HTTP Basic (API uses tokens or custom auth)
        http.httpBasic(basic -> basic.disable());
        http.formLogin(form -> form.disable());

        return http.build();
    }
}
