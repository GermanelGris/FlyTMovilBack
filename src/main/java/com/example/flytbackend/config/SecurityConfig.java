// src/main/java/com/example/flytbackend/config/SecurityConfig.java
package com.example.flytbackend.config;

import com.example.flytbackend.filter.JwtFilter;
import com.example.flytbackend.repository.ClienteRepository;
import com.example.flytbackend.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyectar JwtFilter de forma perezosa para evitar ciclo de dependencias
    @Autowired
    @Lazy
    private JwtFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Habilitar CORS para que Spring use el CorsConfigurationSource de CorsConfig
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permitir todo temporalmente para aislar problema 403
                .anyRequest().permitAll()
            )
            // No crear sesión (API REST con JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Añadir filtro JWT antes del filtro de username/password
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para cargar usuarios (Cliente) por email — ahora devuelve UserDetails
    @Bean
    public UserDetailsService userDetailsService(ClienteRepository clienteRepository) {
        return username -> {
            Cliente c = clienteRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado: " + username));

            String roles = c.getRoles() == null ? "CLIENTE" : c.getRoles();
            List<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> "ROLE_" + s.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetails user = User.withUsername(c.getEmail())
                    .password(c.getContrasena())
                    .authorities(authorities)
                    .build();

            return user;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}