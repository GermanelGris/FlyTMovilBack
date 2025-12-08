package com.example.flytbackend.service;

                    import com.example.flytbackend.entity.Aeropuerto;
                    import com.example.flytbackend.repository.AeropuertoRepository;
                    import org.springframework.stereotype.Service;
                    import org.springframework.transaction.annotation.Transactional;

                    import java.util.List;
                    import java.util.Optional;

                    @Service
                    @Transactional
                    public class AeropuertoService {

                        private final AeropuertoRepository repository;

                        public AeropuertoService(AeropuertoRepository repository) {
                            this.repository = repository;
                        }

                        public List<Aeropuerto> findAll() {
                            return repository.findAll();
                        }

                        public Optional<Aeropuerto> findById(Integer id) {
                            return repository.findById(id);
                        }

                        public Aeropuerto create(Aeropuerto a) {
                            a.setIdAeropuerto(null); // asegurar creación
                            return repository.save(a);
                        }

                        public Optional<Aeropuerto> update(Integer id, Aeropuerto data) {
                            return repository.findById(id).map(existing -> {
                                existing.setNombre(data.getNombre());
                                existing.setCiudad(data.getCiudad());
                                existing.setPais(data.getPais());
                                existing.setCodigoIata(data.getCodigoIata());
                                return repository.save(existing);
                            });
                        }

                        public void delete(Integer id) {
                            repository.deleteById(id);
                        }

                        public List<Aeropuerto> buscarPorTexto(String q) {
                            if (q == null || q.trim().isEmpty()) return List.of();
                            return repository.buscarPorTexto(q.trim());
                        }
                    }