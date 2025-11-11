package ar.edu.utn.frc.backend.grupo114.solicitudes.repository;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para tipos de estado (solicitud o tramo).
 */
@Repository
public interface TipoEstadoRepository extends JpaRepository<TipoEstado, Long> {

    // Permite buscar un tipo de estado por nombre
    TipoEstado findByNombre(String nombre);
}
