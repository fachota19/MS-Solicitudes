package ar.edu.utn.frc.backend.grupo114.solicitudes.repository;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de rutas de transporte.
 */
@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    // Buscar ruta asociada a una solicitud
    Ruta findBySolicitudId(Long solicitudId);
}
