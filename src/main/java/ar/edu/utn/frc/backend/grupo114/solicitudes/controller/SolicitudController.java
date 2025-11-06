package ar.edu.utn.frc.backend.grupo114.solicitudes.controller;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que gestiona las operaciones CRUD sobre Solicitudes.
 * Sigue las convenciones RESTful y devuelve respuestas HTTP adecuadas.
 */
@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*") // permite peticiones desde el frontend o gateway
public class SolicitudController {

    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    /**
     * Obtener todas las solicitudes
     * Ejemplo: GET /api/solicitudes
     */
    @GetMapping
    public ResponseEntity<List<Solicitud>> listar() {
        List<Solicitud> solicitudes = service.listarTodas();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener una solicitud por su ID
     * Ejemplo: GET /api/solicitudes/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        Solicitud solicitud = service.obtenerPorId(id);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Crear una nueva solicitud
     * Ejemplo: POST /api/solicitudes
     * Body JSON:
     * {
     *   "fechaCreacion": "2025-11-06",
     *   "estado": "PENDIENTE",
     *   "costoEstimado": 15000.0
     * }
     */
    @PostMapping
    public ResponseEntity<Solicitud> crear(@RequestBody Solicitud solicitud) {
        Solicitud nueva = service.crear(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    /**
     * Actualizar una solicitud existente
     * Ejemplo: PUT /api/solicitudes/3
     */
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(@PathVariable Long id, @RequestBody Solicitud solicitud) {
        Solicitud actualizada = service.actualizar(id, solicitud);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Eliminar una solicitud
     * Ejemplo: DELETE /api/solicitudes/3
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
