package com.example.flytbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.flytbackend.entity.Aerolinea;

public interface AerolineaRepository extends JpaRepository<Aerolinea, Integer> {
}