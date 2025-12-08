package com.example.flytbackend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.example.flytbackend.repository.AerolineaRepository;
import com.example.flytbackend.entity.Aerolinea;

@Service
public class AerolineaService {
    private final AerolineaRepository repo;

    public AerolineaService(AerolineaRepository repo) {
        this.repo = repo;
    }

    public List<Aerolinea> findAll() { return repo.findAll(); }
    public Optional<Aerolinea> findById(Integer id) { return repo.findById(id); }
    public Aerolinea create(Aerolinea a) { return repo.save(a); }
    public Optional<Aerolinea> update(Integer id, Aerolinea a) {
        return repo.findById(id).map(existing -> {
            a.idAerolinea = id;
            return repo.save(a);
        });
    }
    public void delete(Integer id) { repo.deleteById(id); }
}