package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.ContenedorRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.SolicitudRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.ContenedorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j  // ✅ AGREGADO: Habilita el logger
@Service
@Transactional
public class ContenedorServiceImpl implements ContenedorService {

    private final ContenedorRepository repository;
    private final SolicitudRepository solicitudRepository;  // ✅ AGREGADO

    // ✅ CONSTRUCTOR ACTUALIZADO
    public ContenedorServiceImpl(
            ContenedorRepository repository,
            SolicitudRepository solicitudRepository
    ) {
        this.repository = repository;
        this.solicitudRepository = solicitudRepository;
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
        log.info("Eliminando contenedor con ID: {}", id);
        
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Contenedor no existe");
        }
        
        // ✅ VALIDAR si hay solicitudes asociadas
        long solicitudesAsociadas = solicitudRepository.countByContenedorId(id);
        
        if (solicitudesAsociadas > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "No se puede eliminar el contenedor porque tiene " + 
                solicitudesAsociadas + " solicitud(es) asociada(s)");
        }
        
        repository.deleteById(id);
        log.info("Contenedor con ID {} eliminado exitosamente", id);
    }
}