package com.example.flytbackend.service.impl;

import com.example.flytbackend.controller.dto.AuthResponse;
import com.example.flytbackend.controller.dto.LoginRequest;
import com.example.flytbackend.controller.dto.RegisterRequest;
import com.example.flytbackend.entity.Cliente;
import com.example.flytbackend.repository.ClienteRepository;
import com.example.flytbackend.service.AuthService;
import com.example.flytbackend.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear y guardar el nuevo cliente
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getFono());  // Cambio: setTelefono en vez de setFono
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setContrasena(passwordEncoder.encode(request.getPassword()));  // Cambio: setContrasena en vez de setPassword

        clienteRepository.save(cliente);

        // Generar token JWT
        String token = jwtService.generateToken(cliente.getEmail());

        return new AuthResponse(token, "Usuario registrado exitosamente");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Buscar el cliente
        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token JWT
        String token = jwtService.generateToken(cliente.getEmail());

        return new AuthResponse(token, "Login exitoso");
    }
}