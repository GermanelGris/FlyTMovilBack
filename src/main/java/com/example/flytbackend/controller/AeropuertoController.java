package com.example.flytbackend.controller;

    import com.example.flytbackend.entity.Aeropuerto;
    import com.example.flytbackend.service.AeropuertoService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/aeropuertos")
    public class AeropuertoController {

        private final AeropuertoService service;

        public AeropuertoController(AeropuertoService service) {
            this.service = service;
        }

        @GetMapping
        public List<Aeropuerto> all() {
            return service.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Aeropuerto> get(@PathVariable Integer id) {
            return service.findById(id)
                    .map(a -> ResponseEntity.ok(a))
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<Aeropuerto> create(@RequestBody Aeropuerto a) {
            if (a.getNombre() == null || a.getNombre().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Aeropuerto saved = service.create(a);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Aeropuerto> update(@PathVariable Integer id, @RequestBody Aeropuerto a) {
            return service.update(id, a)
                    .map(updated -> ResponseEntity.ok(updated))
                    .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Integer id) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/buscar")
        public ResponseEntity<List<Aeropuerto>> buscar(@RequestParam("q") String query) {
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            List<Aeropuerto> resultados = service.buscarPorTexto(query);
            return ResponseEntity.ok(resultados);
        }
    }