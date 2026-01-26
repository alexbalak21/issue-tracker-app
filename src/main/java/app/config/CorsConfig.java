package app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    // Only allow requests from static index.html

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Only allow requests from the static index.html served by backend
        configuration.setAllowedOrigins(List.of()); // No external origins allowed
        configuration.setAllowCredentials(false);

        configuration.setAllowedMethods(List.of("GET"));
        configuration.setAllowedHeaders(List.of());
        configuration.setExposedHeaders(List.of());
        configuration.setMaxAge(0L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Only allow CORS for /static/index.html
        source.registerCorsConfiguration("/static/index.html", configuration);

        return source;
    }
}
