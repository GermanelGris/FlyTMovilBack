package com.example.flytbackend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.time.LocalDate;
import com.example.flytbackend.service.VueloProgramadoService;
import com.example.flytbackend.entity.VueloProgramado;

@RestController
@RequestMapping("/api/vuelos-programados")
public class VueloProgramadoController {
    private final VueloProgramadoService service;

    public VueloProgramadoController(VueloProgramadoService service) { this.service = service; }

    @GetMapping
    public List<VueloProgramado> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<VueloProgramado> get(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Nuevo endpoint de búsqueda: /api/vuelos-programados/search?origen=...&destino=...&fechaSalida=2025-12-07
    @GetMapping("/search")
    public List<VueloProgramado> search(
        @RequestParam String origen,
        @RequestParam(required = false) String destino,
        @RequestParam String fechaSalida
    ) {
        LocalDate fecha = LocalDate.parse(fechaSalida);
        // Convertir cadena vacía a null para activar la cláusula (:destino IS NULL OR ...)
        if (destino != null && destino.isBlank()) destino = null;
        return service.search(origen, destino, fecha);
    }

    @PostMapping
    public ResponseEntity<VueloProgramado> create(@RequestBody VueloProgramado v) {
        VueloProgramado saved = service.create(v);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VueloProgramado> update(@PathVariable Integer id, @RequestBody VueloProgramado v) {
        return service.update(id, v).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}