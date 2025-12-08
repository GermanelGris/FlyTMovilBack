package com.example.flytbackend.repository;

import com.example.flytbackend.entity.Aeropuerto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Integer> {

    @Query("""
        SELECT a FROM Aeropuerto a WHERE
        LOWER(a.ciudad) LIKE LOWER(CONCAT('%', :q, '%')) OR
        LOWER(a.pais) LIKE LOWER(CONCAT('%', :q, '%')) OR
        LOWER(a.codigoIata) LIKE LOWER(CONCAT('%', :q, '%')) OR
        LOWER(a.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
        """)
    List<Aeropuerto> buscarPorTexto(@Param("q") String q);
}