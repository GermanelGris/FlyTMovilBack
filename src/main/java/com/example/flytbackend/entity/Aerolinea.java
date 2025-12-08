package com.example.flytbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "aerolinea")
public class Aerolinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aerolinea", columnDefinition = "INT UNSIGNED")
    public Integer idAerolinea;

    @Column(name = "nombre", length = 100, nullable = false)
    public String nombre;

    @Column(name = "codigo", length = 10, nullable = false, unique = true)
    public String codigo;
}