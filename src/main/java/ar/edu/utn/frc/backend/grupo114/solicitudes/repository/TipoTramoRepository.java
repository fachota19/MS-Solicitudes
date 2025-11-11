package ar.edu.utn.frc.backend.grupo114.solicitudes.repository;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoTramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para los tipos de tramo (e.g., entre depósitos, cliente-depósito).
 */
@Repository
public interface TipoTramoRepository extends JpaRepository<TipoTramo, Long> {

    // Permite buscar un tipo de tramo por nombre
    TipoTramo findByNombre(String nombre);
}
