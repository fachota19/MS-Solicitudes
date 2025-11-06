package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import java.util.List;

/**
 * Interfaz que define las operaciones de negocio para gestionar Solicitudes.
 * Esta capa intermedia desacopla el controlador del repositorio.
 */
public interface SolicitudService {

    /**
     * Devuelve todas las solicitudes registradas.
     */
    List<Solicitud> listarTodas();

    /**
     * Busca una solicitud específica por su ID.
     */
    Solicitud obtenerPorId(Long id);

    /**
     * Crea una nueva solicitud.
     */
    Solicitud crear(Solicitud solicitud);

    /**
     * Actualiza una solicitud existente.
     */
    Solicitud actualizar(Long id, Solicitud solicitud);

    /**
     * Elimina una solicitud por ID.
     */
    void eliminar(Long id);
}
