package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Tramo;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.SolicitudRepository; 
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.TipoEstadoRepository; 
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.TramoRepository; 
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TramoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TramoServiceImpl implements TramoService {

    private final TramoRepository tramoRepository;
    private final TipoEstadoRepository tipoEstadoRepository;
    private final SolicitudRepository solicitudRepository; // Necesario para actualizar la entidad padre

    public TramoServiceImpl(TramoRepository tramoRepository,
                            TipoEstadoRepository tipoEstadoRepository,
                            SolicitudRepository solicitudRepository) {
        this.tramoRepository = tramoRepository;
        this.tipoEstadoRepository = tipoEstadoRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public Optional<Tramo> iniciarTramo(Long id, LocalDateTime fechaHoraInicioReal) {
        return tramoRepository.findById(id).map(tramo -> {
            
            // 1. Validar estado actual del tramo
            if (!"PENDIENTE".equals(tramo.getEstado().getNombre())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El tramo debe estar en estado PENDIENTE para iniciarlo");
            }

            // 2. Actualizar Tramo
            tramo.setFechaHoraInicioReal(fechaHoraInicioReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("EN_PROCESO"));
            Tramo tramoGuardado = tramoRepository.save(tramo);

            // 3. Actualizar Solicitud (Cascada): Si empieza el viaje, Solicitud -> EN_TRANSITO
            if (tramo.getRuta() != null && tramo.getRuta().getSolicitud() != null) {
                Solicitud solicitud = tramo.getRuta().getSolicitud();
                
                // Solo cambiamos si estaba esperando (Programada)
                if ("PROGRAMADA".equals(solicitud.getEstado().getNombre())) {
                    solicitud.setEstado(tipoEstadoRepository.findByNombre("EN_TRANSITO"));
                    solicitudRepository.save(solicitud);
                }
            }

            return tramoGuardado;
        });
    }

    @Override
    public Optional<Tramo> finalizarTramo(Long id, LocalDateTime fechaHoraFinReal) {
        return tramoRepository.findById(id).map(tramo -> {
            
            // 1. Validaciones
            if (!"EN_PROCESO".equals(tramo.getEstado().getNombre())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El tramo debe estar EN_PROCESO para finalizarlo");
            }
            if (tramo.getFechaHoraInicioReal() != null && fechaHoraFinReal.isBefore(tramo.getFechaHoraInicioReal())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La fecha de fin no puede ser anterior a la de inicio");
            }

            // 2. Actualizar Tramo
            tramo.setFechaHoraFinReal(fechaHoraFinReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("COMPLETADO"));
            Tramo tramoGuardado = tramoRepository.save(tramo);

            // 3. Actualizar Solicitud (Cascada): Verificar si se completó todo el viaje
            if (tramo.getRuta() != null && tramo.getRuta().getSolicitud() != null) {
                Solicitud solicitud = tramo.getRuta().getSolicitud();
                
                // Verificar si TODOS los tramos de esta ruta están COMPLETADOS
                boolean viajeFinalizado = solicitud.getRuta().getTramos().stream()
                        .allMatch(t -> "COMPLETADO".equals(t.getEstado().getNombre()));

                if (viajeFinalizado) {
                    solicitud.setEstado(tipoEstadoRepository.findByNombre("ENTREGADA"));
                    // Aquí podrías setear también fecha fin real de solicitud si tu modelo lo tiene
                    solicitudRepository.save(solicitud);
                }
            }

            return tramoGuardado;
        });
    }
}