package com.example.flytbackend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.example.flytbackend.service.AerolineaService;
import com.example.flytbackend.entity.Aerolinea;

@RestController
@RequestMapping("/api/aerolineas")
public class AerolineaController {
    private final AerolineaService service;

    public AerolineaController(AerolineaService service) { this.service = service; }

    @GetMapping
    public List<Aerolinea> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Aerolinea> get(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aerolinea> create(@RequestBody Aerolinea a) {
        Aerolinea saved = service.create(a);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aerolinea> update(@PathVariable Integer id, @RequestBody Aerolinea a) {
        return service.update(id, a).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}