package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SolicitudService {

    // 🔹 Listar todas las solicitudes
    List<Solicitud> listarTodas();

    // 🔹 Obtener una solicitud por ID
    Optional<Solicitud> obtenerPorId(Long id);

    // 🔹 Crear una nueva solicitud
    Solicitud crear(Solicitud solicitud);

    // 🔹 Eliminar solicitud
    void eliminar(Long id);

    // 🔹 Obtener seguimiento (estado + tramos + ruta)
    Optional<Map<String, Object>> obtenerSeguimiento(Long id);
    Optional<Map<String, Object>> obtenerSeguimientoPorNumero(String numeroSeguimiento);

    // 🔹 Asignar una ruta a una solicitud
    Solicitud asignarRuta(Long solicitudId, Long rutaId);

    // 🔹 Método opcional (para compatibilidad con versiones anteriores)
    default Solicitud actualizar(Long id, Solicitud solicitud) {
        throw new UnsupportedOperationException("No implementado");
    }
}
