package com.example.flytbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vuelo")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vuelo", columnDefinition = "INT UNSIGNED")
    public Integer idVuelo;

    @Column(name = "codigo_vuelo", length = 10, nullable = false, unique = true)
    public String codigoVuelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_aerolinea")
    public Aerolinea aerolinea;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_origen")
    public Aeropuerto origen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_destino")
    public Aeropuerto destino;

    @Column(name = "duracion_min", nullable = false)
    public Integer duracionMin;
}