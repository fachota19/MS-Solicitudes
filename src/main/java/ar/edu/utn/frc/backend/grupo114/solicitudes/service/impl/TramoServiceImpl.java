package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TramoService;
import org.springframework.stereotype.Service;

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
            tramo.setFechaHoraInicioReal(fechaHoraInicioReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("EN_PROCESO"));
            return tramoRepository.save(tramo);
        });
    }

    @Override
    public Optional<Tramo> finalizarTramo(Long id, LocalDateTime fechaHoraFinReal) {
        return tramoRepository.findById(id).map(tramo -> {
            tramo.setFechaHoraFinReal(fechaHoraFinReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("COMPLETADO"));
            return tramoRepository.save(tramo);
        });
    }
}
