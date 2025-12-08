package com.example.flytbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.flytbackend.entity.Vuelo;

public interface VueloRepository extends JpaRepository<Vuelo, Integer> {
}