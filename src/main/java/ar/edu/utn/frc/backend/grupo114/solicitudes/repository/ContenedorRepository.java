package ar.edu.utn.frc.backend.grupo114.solicitudes.repository;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD sobre contenedores.
 */
@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
}
