package com.example.flytbackend.controller;

import com.example.flytbackend.controller.dto.LoginRequest;
import com.example.flytbackend.controller.dto.RegisterRequest;
import com.example.flytbackend.entity.Cliente;
import com.example.flytbackend.repository.ClienteRepository;
import com.example.flytbackend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        // Log request values for debugging
        log.debug("Incoming register: email={}, fotoPerfilUri={}, roles={}",
                request.getEmail(),
                request.getFotoPerfilUri(),
                request.getRoles());

        // Validación mínima adicional
        if (clienteRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "El correo ya está registrado"));
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getFono());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setContrasena(passwordEncoder.encode(request.getPassword()));

        // Convertir roles (List<String>) a cadena comma-separated y normalizar a mayúsculas
        List<String> rolesList = request.getRoles();
        if (rolesList == null || rolesList.isEmpty()) {
            cliente.setRoles("CLIENTE");
        } else {
            String rolesCsv = rolesList.stream()
                    .map(s -> s.replace("ROLE_", "").trim().toUpperCase())
                    .collect(Collectors.joining(","));
            cliente.setRoles(rolesCsv);
        }

        // Guardar solo la URI/ruta que envía el front
        if (request.getFotoPerfilUri() != null && !request.getFotoPerfilUri().isBlank()) {
            cliente.setFotoPerfil(request.getFotoPerfilUri());
        }

        clienteRepository.save(cliente);

        String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getId());
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Usuario registrado correctamente");
        resp.put("token", token);
        resp.put("id", cliente.getId());
        resp.put("email", cliente.getEmail());
        resp.put("fotoPerfil", cliente.getFotoPerfil());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(request.getEmail());
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales inválidas"));
        }

        Cliente cliente = clienteOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), cliente.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales inválidas"));
        }

        String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getId());
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Login exitoso");
        resp.put("token", token);
        resp.put("id", cliente.getId());
        resp.put("nombre", cliente.getNombre());
        resp.put("apellido", cliente.getApellido());
        resp.put("email", cliente.getEmail());
        resp.put("fotoPerfil", cliente.getFotoPerfil());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(name = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token no proporcionado"));
        }
        String token = authorization.substring(7);
        try {
            // Extraer email y userId del token
            String email = jwtUtil.extractEmail(token);
            Long uid = null;
            try { uid = jwtUtil.extractUserId(token); } catch (Exception ignored) {}

            // Buscar por email preferentemente
            Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
            if (clienteOpt.isEmpty()) {
                // fallback: buscar por id si existe
                if (uid != null) clienteOpt = clienteRepository.findById(uid);
            }

            if (clienteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Usuario no encontrado"));
            }

            Cliente cliente = clienteOpt.get();
            Map<String, Object> out = new HashMap<>();
            out.put("id", cliente.getId());
            out.put("nombre", cliente.getNombre());
            out.put("apellido", cliente.getApellido());
            out.put("email", cliente.getEmail());
            out.put("fotoPerfil", cliente.getFotoPerfil());
            out.put("roles", cliente.getRoles());
            return ResponseEntity.ok(out);
        } catch (Exception ex) {
            log.warn("Error validando token en /me: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token inválido"));
        }
    }
}