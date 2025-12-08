package com.example.flytbackend.security;

        import com.auth0.jwt.JWT;
        import com.auth0.jwt.algorithms.Algorithm;
        import com.auth0.jwt.interfaces.DecodedJWT;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Component;

        import java.util.Date;

        @Component
        public class JwtUtil {

            @Value("${jwt.secret}")
            private String secret;

            @Value("${jwt.issuer:flytbackend}")
            private String issuer;

            // Nueva sobrecarga compatible con Long
            public String generateToken(String email, Long id) {
                return buildToken(email, id);
            }

            // Mantener compatibilidad con Integer
            public String generateToken(String email, Integer id) {
                return buildToken(email, id == null ? null : id.longValue());
            }

            private String buildToken(String email, Long id) {
                Algorithm alg = Algorithm.HMAC256(secret);
                var builder = JWT.create()
                        .withIssuer(issuer)
                        .withSubject(email)
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 4));
                if (id != null) {
                    builder.withClaim("uid", id);
                }
                return builder.sign(alg);
            }

            public DecodedJWT verify(String token) {
                return JWT.require(Algorithm.HMAC256(secret))
                        .withIssuer(issuer)
                        .build()
                        .verify(token);
            }

            public String extractEmail(String token) {
                return verify(token).getSubject();
            }

            public Long extractUserId(String token) {
                return verify(token).getClaim("uid").asLong();
            }
        }