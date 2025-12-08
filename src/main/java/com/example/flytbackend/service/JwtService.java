// java
                    package com.example.flytbackend.service;

                    import com.auth0.jwt.JWT;
                    import com.auth0.jwt.algorithms.Algorithm;
                    import com.auth0.jwt.interfaces.DecodedJWT;
                    import org.springframework.beans.factory.annotation.Value;
                    import org.springframework.stereotype.Service;

                    import java.util.Date;

                    @Service
                    public class JwtService {

                        @Value("${jwt.secret}")
                        private String secret;

                        @Value("${jwt.issuer:flytbackend}")
                        private String issuer;

                        public String generateToken(String username) {
                            return JWT.create()
                                    .withSubject(username)
                                    .withIssuer(issuer)
                                    .withIssuedAt(new Date())
                                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 4))
                                    .sign(Algorithm.HMAC256(secret));
                        }

                        public String extractUsername(String token) {
                            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                                    .withIssuer(issuer)
                                    .build()
                                    .verify(token);
                            return jwt.getSubject();
                        }

                        public Long extractUserId(String token) {
                            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                                    .withIssuer(issuer)
                                    .build()
                                    .verify(token);
                            Long uid = jwt.getClaim("uid").asLong();
                            if (uid != null) return uid;
                            try {
                                return Long.parseLong(jwt.getSubject());
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        }

                        public boolean isValid(String token, String username) {
                            try {
                                String sub = extractUsername(token);
                                DecodedJWT jwt = JWT.decode(token);
                                return sub.equals(username) && jwt.getExpiresAt().after(new Date());
                            } catch (Exception e) {
                                return false;
                            }
                        }
                    }