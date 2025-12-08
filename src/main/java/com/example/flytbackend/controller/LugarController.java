package com.example.flytbackend.controller;

import com.example.flytbackend.entity.Aeropuerto;
import com.example.flytbackend.service.AeropuertoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lugares")
public class LugarController {

    private final AeropuertoService aeropuertoService;

    public LugarController(AeropuertoService aeropuertoService) {
        this.aeropuertoService = aeropuertoService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Aeropuerto>> buscar(@RequestParam("q") String q) {
        if (q == null || q.trim().isEmpty()) return ResponseEntity.ok(List.of());
        return ResponseEntity.ok(aeropuertoService.buscarPorTexto(q));
    }

    @GetMapping
    public List<Aeropuerto> listar() { return aeropuertoService.findAll(); }
}