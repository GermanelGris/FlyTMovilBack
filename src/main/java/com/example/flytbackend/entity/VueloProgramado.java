package com.example.flytbackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vuelo_programado")
public class VueloProgramado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vuelo_prog", columnDefinition = "INT UNSIGNED")
    public Integer idVueloProg;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vuelo")
    public Vuelo vuelo;

    @Column(name = "fecha_salida", nullable = false)
    public LocalDate fechaSalida;

    @Column(name = "hora_salida", nullable = false)
    public LocalTime horaSalida;

    @Column(name = "fecha_llegada", nullable = false)
    public LocalDate fechaLlegada;

    @Column(name = "hora_llegada", nullable = false)
    public LocalTime horaLlegada;

    @Column(name = "precio", precision = 10, scale = 2, nullable = false)
    public BigDecimal precio;

    @Column(name = "asientos_totales", nullable = false)
    public Integer asientosTotales;

    @Column(name = "asientos_disp", nullable = false)
    public Integer asientosDisp;

    @Column(name = "numero_escalas")
    public Integer numeroEscalas;
}