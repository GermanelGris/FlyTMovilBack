package com.example.flytbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // Permite que la app envíe credenciales (el token JWT)
        cfg.setAllowCredentials(true);

        // Acepta peticiones desde cualquier origen (con puertos diferentes)
        cfg.setAllowedOriginPatterns(List.of("*"));

        // Define los métodos HTTP permitidos
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));

        // Permite todas las cabeceras, incluyendo "Authorization"
        cfg.setAllowedHeaders(List.of("*"));

        // Exponer la cabecera Authorization al cliente (útil si el cliente necesita leerla)
        cfg.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}