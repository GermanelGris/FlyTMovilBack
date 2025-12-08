package com.example.flytbackend.service;

        import com.example.flytbackend.entity.Cliente;
        import com.example.flytbackend.repository.ClienteRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

        @Service
        public class ClienteService {

            @Autowired
            private ClienteRepository clienteRepository;

            public List<Cliente> listar() {
                return clienteRepository.findAll();
            }

            public Optional<Cliente> obtenerPorId(Long id) {
                return clienteRepository.findById(id);
            }

            public Cliente crear(Cliente cliente) {
                return clienteRepository.save(cliente);
            }

            public Cliente actualizar(Long id, Cliente cliente) {
                cliente.setId(id);
                return clienteRepository.save(cliente);
            }

            public void eliminar(Long id) {
                clienteRepository.deleteById(id);
            }
        }