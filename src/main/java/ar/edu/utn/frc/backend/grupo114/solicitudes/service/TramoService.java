package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Tramo;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TramoService {

    // 🔹 Iniciar un tramo (establece hora real y estado EN_PROCESO)
    Optional<Tramo> iniciarTramo(Long id, LocalDateTime fechaHoraInicioReal);

    // 🔹 Finalizar un tramo (establece hora fin y estado COMPLETADO)
    Optional<Tramo> finalizarTramo(Long id, LocalDateTime fechaHoraFinReal);
}