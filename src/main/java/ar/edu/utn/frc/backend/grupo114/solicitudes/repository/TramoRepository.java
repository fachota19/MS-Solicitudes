package ar.edu.utn.frc.backend.grupo114.solicitudes.repository;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para los tramos de una ruta.
 */
@Repository
public interface TramoRepository extends JpaRepository<Tramo, Long> {

    // Buscar todos los tramos de una ruta específica
    List<Tramo> findByRutaId(Long rutaId);
}
