package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TramoService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TramoServiceImpl implements TramoService {

    private final TramoRepository tramoRepository;
    private final TipoEstadoRepository tipoEstadoRepository;

    public TramoServiceImpl(TramoRepository tramoRepository, TipoEstadoRepository tipoEstadoRepository) {
        this.tramoRepository = tramoRepository;
        this.tipoEstadoRepository = tipoEstadoRepository;
    }

    @Override
    public Optional<Tramo> iniciarTramo(Long id, LocalDateTime fechaHoraInicioReal) {
        return tramoRepository.findById(id).map(tramo -> {
            // 🔥 Validar que esté PENDIENTE
            if (!"PENDIENTE".equals(tramo.getEstado().getNombre())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El tramo debe estar en estado PENDIENTE para iniciarlo");
            }
            
            tramo.setFechaHoraInicioReal(fechaHoraInicioReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("EN_PROCESO"));
            return tramoRepository.save(tramo);
        });
    }

    @Override
    public Optional<Tramo> finalizarTramo(Long id, LocalDateTime fechaHoraFinReal) {
        return tramoRepository.findById(id).map(tramo -> {
            // 🔥 Validar que esté EN_PROCESO
            if (!"EN_PROCESO".equals(tramo.getEstado().getNombre())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El tramo debe estar EN_PROCESO para finalizarlo");
            }
            
            // 🔥 Validar que fecha fin > fecha inicio
            if (tramo.getFechaHoraInicioReal() != null && 
                fechaHoraFinReal.isBefore(tramo.getFechaHoraInicioReal())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de fin no puede ser anterior a la de inicio");
            }
            
            tramo.setFechaHoraFinReal(fechaHoraFinReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("COMPLETADO"));
            return tramoRepository.save(tramo);
        });
    }
}
