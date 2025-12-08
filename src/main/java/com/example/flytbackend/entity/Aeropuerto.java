package com.example.flytbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aeropuerto")
@Getter
@Setter
public class Aeropuerto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeropuerto", columnDefinition = "INT UNSIGNED")
    private Integer idAeropuerto;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 100)
    private String pais;

    @Column(name = "codigo_iata", length = 3, unique = true)
    private String codigoIata;
}