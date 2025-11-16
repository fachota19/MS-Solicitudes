package ar.edu.utn.frc.backend.grupo114.solicitudes.repository;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio principal de solicitudes de transporte.
 */
@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // Buscar por número de seguimiento (si se usa para tracking)
    Solicitud findByNumeroSeguimiento(String numeroSeguimiento);

    long countByContenedorId(Long contenedorId);

}



