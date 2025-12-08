package com.example.flytbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import com.example.flytbackend.entity.VueloProgramado;

public interface VueloProgramadoRepository extends JpaRepository<VueloProgramado, Integer> {
    // Consulta: origen obligatorio, destino opcional, y fechaSalida >= fecha indicada
    @Query("SELECT vp FROM VueloProgramado vp WHERE vp.vuelo.origen.ciudad = :origen AND (:destino IS NULL OR vp.vuelo.destino.ciudad = :destino) AND vp.fechaSalida >= :fechaSalida")
    List<VueloProgramado> buscarVuelos(
        @Param("origen") String origen,
        @Param("destino") String destino,
        @Param("fechaSalida") LocalDate fechaSalida
    );
}