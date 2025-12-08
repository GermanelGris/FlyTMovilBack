// java
        package com.example.flytbackend.controller;

        import com.example.flytbackend.entity.Cliente;
        import com.example.flytbackend.repository.ClienteRepository;
        import com.example.flytbackend.service.ClienteService;
        import com.example.flytbackend.security.JwtUtil;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.util.StringUtils;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

        import java.net.URI;
        import java.util.List;
        import java.util.Optional;

        @CrossOrigin(origins = "http://localhost:5174")
        @RestController
        @RequestMapping("/api/clientes")
        public class ClienteController {

            @Autowired
            private ClienteService clienteService;

            @Autowired
            private JwtUtil jwtUtil;

            @Autowired
            private ClienteRepository clienteRepository;

            @GetMapping
            public ResponseEntity<List<Cliente>> listar() {
                return ResponseEntity.ok(clienteService.listar());
            }

            @GetMapping("/{id}")
            public ResponseEntity<Cliente> obtener(@PathVariable Long id) {
                Optional<Cliente> optional = clienteService.obtenerPorId(id);
                return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
            }

            @PostMapping
            public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
                Cliente creado = clienteService.crear(cliente);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(creado.getId()).toUri();
                return ResponseEntity.created(location).body(creado);
            }

            @PutMapping("/{id}")
            public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
                Optional<Cliente> existente = clienteService.obtenerPorId(id);
                if (existente.isEmpty()) return ResponseEntity.notFound().build();
                return ResponseEntity.ok(clienteService.actualizar(id, cliente));
            }

            @DeleteMapping("/{id}")
            public ResponseEntity<Void> eliminar(@PathVariable Long id) {
                Optional<Cliente> existente = clienteService.obtenerPorId(id);
                if (existente.isEmpty()) return ResponseEntity.notFound().build();
                clienteService.eliminar(id);
                return ResponseEntity.noContent().build();
            }

            @GetMapping("/me")
            public ResponseEntity<?> getMe(@RequestHeader(name = "Authorization", required = false) String authHeader) {
                if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                String token = authHeader.substring("Bearer ".length()).trim();
                Long userId = jwtUtil.extractUserId(token);
                if (userId == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                Optional<Cliente> cliente = clienteRepository.findById(userId);
                return cliente.<ResponseEntity<?>>map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
        }