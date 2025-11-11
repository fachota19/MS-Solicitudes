package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.ContenedorRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.ContenedorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ContenedorServiceImpl implements ContenedorService {

    private final ContenedorRepository repository;

    public ContenedorServiceImpl(ContenedorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Contenedor> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Contenedor obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenedor no encontrado"));
    }

    @Override
    public Contenedor crear(Contenedor contenedor) {
        return repository.save(contenedor);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenedor no existe");
        repository.deleteById(id);
    }
}
