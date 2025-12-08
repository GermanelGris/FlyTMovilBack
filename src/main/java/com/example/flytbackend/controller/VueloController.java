package com.example.flytbackend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.example.flytbackend.service.VueloService;
import com.example.flytbackend.entity.Vuelo;

@RestController
@RequestMapping("/api/vuelos")
public class VueloController {
    private final VueloService service;

    public VueloController(VueloService service) { this.service = service; }

    @GetMapping
    public List<Vuelo> all() { return service.findAll(); }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Vuelo> get(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vuelo> create(@RequestBody Vuelo v) {
        Vuelo saved = service.create(v);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vuelo> update(@PathVariable Integer id, @RequestBody Vuelo v) {
        return service.update(id, v).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}