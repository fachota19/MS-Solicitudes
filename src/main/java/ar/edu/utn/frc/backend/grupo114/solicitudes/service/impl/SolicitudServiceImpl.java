package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.SolicitudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Service
@Transactional
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final TipoEstadoRepository tipoEstadoRepository;
    private final RutaRepository rutaRepository;

    public SolicitudServiceImpl(
            SolicitudRepository solicitudRepository,
            TipoEstadoRepository tipoEstadoRepository,
            RutaRepository rutaRepository
    ) {
        this.solicitudRepository = solicitudRepository;
        this.tipoEstadoRepository = tipoEstadoRepository;
        this.rutaRepository = rutaRepository;
    }

    @Override
    public List<Solicitud> listarTodas() {
        log.info("Listando todas las solicitudes");
        return solicitudRepository.findAll();
    }

    @Override
    public Optional<Solicitud> obtenerPorId(Long id) {
        log.info("Buscando solicitud con ID: {}", id);
        return solicitudRepository.findById(id);
    }

    @Override
    public Solicitud crear(Solicitud solicitud) {
        log.info("Creando nueva solicitud para cliente: {}", solicitud.getClienteId());
        
        // Generar número de seguimiento si no existe
        if (solicitud.getNumeroSeguimiento() == null || solicitud.getNumeroSeguimiento().isEmpty()) {
            solicitud.setNumeroSeguimiento("SOL-" + System.currentTimeMillis());
        }

        // Asignar estado PENDIENTE si no tiene
        if (solicitud.getEstado() == null) {
            TipoEstado estadoPendiente = tipoEstadoRepository.findByNombre("PENDIENTE");
            if (estadoPendiente == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "No se encontró el estado PENDIENTE en la base de datos");
            }
            solicitud.setEstado(estadoPendiente);
        }

        Solicitud nuevaSolicitud = solicitudRepository.save(solicitud);
        log.info("Solicitud creada exitosamente con ID: {} y número de seguimiento: {}", 
            nuevaSolicitud.getId(), nuevaSolicitud.getNumeroSeguimiento());
        
        return nuevaSolicitud;
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando solicitud con ID: {}", id);
        
        if (!solicitudRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "No se encontró la solicitud con ID: " + id);
        }
        
        solicitudRepository.deleteById(id);
        log.info("Solicitud con ID {} eliminada exitosamente", id);
    }

    @Override
    public Optional<Map<String, Object>> obtenerSeguimiento(Long id) {
        log.info("Obteniendo seguimiento de solicitud con ID: {}", id);
        
        return solicitudRepository.findById(id).map(s -> {
            Map<String, Object> seguimiento = new LinkedHashMap<>();
            seguimiento.put("id", s.getId());
            seguimiento.put("numeroSeguimiento", s.getNumeroSeguimiento());
            seguimiento.put("estado", s.getEstado() != null ? s.getEstado().getNombre() : "SIN_ESTADO");
            seguimiento.put("origen", s.getOrigenDireccion());
            seguimiento.put("destino", s.getDestinoDireccion());
            seguimiento.put("fechaCreacion", s.getFechaCreacion());
            seguimiento.put("costoEstimado", s.getCostoEstimado());

            if (s.getRuta() != null && s.getRuta().getTramos() != null) {
                seguimiento.put("rutaId", s.getRuta().getId());
                seguimiento.put("cantidadTramos", s.getRuta().getTramos().size());
            } else {
                seguimiento.put("rutaId", null);
                seguimiento.put("cantidadTramos", 0);
            }
            
            return seguimiento;
        });
    }

    @Override
    public Solicitud asignarRuta(Long solicitudId, Long rutaId) {
        log.info("Asignando ruta {} a solicitud {}", rutaId, solicitudId);
        
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Solicitud no encontrada con ID: " + solicitudId));

        Ruta ruta = rutaRepository.findById(rutaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Ruta no encontrada con ID: " + rutaId));

        solicitud.setRuta(ruta);
        Solicitud actualizada = solicitudRepository.save(solicitud);
        
        log.info("Ruta asignada exitosamente a la solicitud {}", solicitudId);
        return actualizada;
    }
}