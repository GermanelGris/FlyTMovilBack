// language: java
package com.example.flytbackend.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class RegisterRequest {
    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String fono;

    @NotNull
    @Past
    private LocalDate fechaNacimiento;

    @NotBlank @Size(min = 6)
    private String password;

    // Ahora roles es una lista (array en JSON)
    @NotNull
    private List<String> roles;

    // Nueva: URI o ruta absoluta de la foto en el dispositivo (si el front la envía)
    @JsonAlias({"fotoPerfil", "fotoPerfilUri"})
    private String fotoPerfilUri;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFono() { return fono; }
    public void setFono(String fono) { this.fono = fono; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public String getFotoPerfilUri() { return fotoPerfilUri; }
    public void setFotoPerfilUri(String fotoPerfilUri) { this.fotoPerfilUri = fotoPerfilUri; }
}