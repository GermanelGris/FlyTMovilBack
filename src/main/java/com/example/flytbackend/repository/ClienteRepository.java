// java
package com.example.flytbackend.repository;

import com.example.flytbackend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findById(Long id);
}