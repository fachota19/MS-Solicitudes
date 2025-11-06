package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.SolicitudRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.SolicitudService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de Solicitudes.
 * Contiene la lógica de negocio que gestiona las operaciones CRUD.
 */
@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository repository;

    // Inyección de dependencias por constructor
    public SolicitudServiceImpl(SolicitudRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Solicitud> listarTodas() {
        return repository.findAll();
    }

    @Override
    public Solicitud obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    @Override
    public Solicitud crear(Solicitud solicitud) {
        // Si la fecha no viene, se inicializa con la actual
        if (solicitud.getFechaCreacion() == null) {
            solicitud.setFechaCreacion(java.time.LocalDate.now());
        }
        // Estado por defecto si no se especifica
        if (solicitud.getEstado() == null) {
            solicitud.setEstado("PENDIENTE");
        }
        return repository.save(solicitud);
    }

    @Override
    public Solicitud actualizar(Long id, Solicitud solicitud) {
        Solicitud existente = obtenerPorId(id);

        if (solicitud.getEstado() != null) {
            existente.setEstado(solicitud.getEstado());
        }
        if (solicitud.getCostoEstimado() != null) {
            existente.setCostoEstimado(solicitud.getCostoEstimado());
        }
        if (solicitud.getCamionId() != null) {
            existente.setCamionId(solicitud.getCamionId());
        }
        if (solicitud.getTarifaId() != null) {
            existente.setTarifaId(solicitud.getTarifaId());
        }
        if (solicitud.getClienteId() != null) {
            existente.setClienteId(solicitud.getClienteId());
        }

        return repository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Solicitud con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}
