package app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${app.security.allowed-origin:http://localhost:8100}")
    private String allowedOrigins;

    private final Environment env;

    public CorsConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        String profile = env.getProperty("spring.profiles.active", "prod");
        if (profile.equalsIgnoreCase("dev")) {
            // In dev, allow origins from env
            configuration.setAllowedOrigins(List.of(allowedOrigins.split(",\s*")));
            configuration.setAllowCredentials(true);
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
            configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
            configuration.setExposedHeaders(List.of());
            configuration.setMaxAge(3600L);
        } else {
            // In prod, disable all external origins
            configuration.setAllowedOrigins(List.of());
            configuration.setAllowCredentials(false);
            configuration.setAllowedMethods(List.of("GET"));
            configuration.setAllowedHeaders(List.of());
            configuration.setExposedHeaders(List.of());
            configuration.setMaxAge(0L);
        }

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
