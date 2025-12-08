package com.example.flytbackend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.example.flytbackend.repository.VueloRepository;
import com.example.flytbackend.entity.Vuelo;

@Service
public class VueloService {
    private final VueloRepository repo;

    public VueloService(VueloRepository repo) { this.repo = repo; }

    public List<Vuelo> findAll() { return repo.findAll(); }
    public Optional<Vuelo> findById(Integer id) { return repo.findById(id); }
    public Vuelo create(Vuelo v) { return repo.save(v); }
    public Optional<Vuelo> update(Integer id, Vuelo v) {
        return repo.findById(id).map(existing -> {
            v.idVuelo = id;
            return repo.save(v);
        });
    }
    public void delete(Integer id) { repo.deleteById(id); }
}