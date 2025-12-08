package com.example.flytbackend.filter;

import com.example.flytbackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    // Constante para la clave usada al cachear UserDetails en la request
    private static final String USER_DETAILS_ATTR = "userDetailsLoaded";

    @Autowired
    private JwtService jwtService;

    // Inyectamos el UserDetailsService de forma perezosa para evitar ciclos
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Ignorar preflight y rutas públicas
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        logger.debug("JwtFilter - Header Authorization: {}", authHeader == null ? "null" : "present");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("JwtFilter - No hay token Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        logger.debug("JwtFilter - Token extraído (preview): {}...", jwt.length() > 20 ? jwt.substring(0, 20) : jwt);

        try {
            final String userEmail = jwtService.extractUsername(jwt);
            logger.debug("JwtFilter - Email extraído: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Intentar recuperar UserDetails cacheado en la misma petición
                Object cached = request.getAttribute(USER_DETAILS_ATTR);
                UserDetails userDetails;
                if (cached instanceof UserDetails) {
                    userDetails = (UserDetails) cached;
                    logger.debug("JwtFilter - Usando UserDetails cacheado en request: {}", userDetails.getUsername());
                } else {
                    // Cargar los detalles del usuario desde la base de datos (solo la primera vez en esta petición)
                    userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                    // Guardar en request para evitar nuevas consultas durante la misma petición
                    request.setAttribute(USER_DETAILS_ATTR, userDetails);
                    logger.debug("JwtFilter - Usuario cargado y cacheado: {}", userDetails.getUsername());
                }

                if (userEmail.equals(userDetails.getUsername())) {
                    logger.debug("JwtFilter - Token válido, estableciendo autenticación");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("JwtFilter - Autenticación establecida correctamente");
                } else {
                    logger.warn("JwtFilter - Token inválido: email no coincide");
                }
            }
        } catch (Exception e) {
            logger.error("JwtFilter - Error al procesar el token JWT: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}