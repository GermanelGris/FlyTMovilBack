package com.example.flytbackend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import com.example.flytbackend.repository.VueloProgramadoRepository;
import com.example.flytbackend.entity.VueloProgramado;

@Service
public class VueloProgramadoService {
    private final VueloProgramadoRepository repo;

    public VueloProgramadoService(VueloProgramadoRepository repo) { this.repo = repo; }

    public List<VueloProgramado> findAll() { return repo.findAll(); }
    public Optional<VueloProgramado> findById(Integer id) { return repo.findById(id); }
    public VueloProgramado create(VueloProgramado v) { return repo.save(v); }
    public Optional<VueloProgramado> update(Integer id, VueloProgramado v) {
        return repo.findById(id).map(existing -> {
            v.idVueloProg = id;
            return repo.save(v);
        });
    }
    public void delete(Integer id) { repo.deleteById(id); }

    // Nuevo: búsqueda por criterios usada desde la HomePage
    public List<VueloProgramado> search(String origen, String destino, LocalDate fechaSalida) {
        return repo.buscarVuelos(origen, destino, fechaSalida);
    }
}