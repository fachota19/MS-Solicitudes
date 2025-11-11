package ar.edu.utn.frc.backend.grupo114.solicitudes.controller;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.SolicitudDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.mapper.SolicitudMapper;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de transporte de contenedores")
@SecurityRequirement(name = "bearerAuth")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @Operation(
        summary = "Listar todas las solicitudes",
        description = "Obtiene todas las solicitudes de transporte (solo OPERADOR)"
    )
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Solicitud>> listarTodas() {
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    @Operation(
        summary = "Obtener solicitud por ID",
        description = "Obtiene los detalles de una solicitud específica"
    )
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> obtenerPorId(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        return solicitudService.obtenerPorId(id)
                .map(SolicitudMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nueva solicitud",
        description = "Permite al cliente crear una solicitud de transporte de contenedor"
    )
    @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<SolicitudDTO> crear(@Valid @RequestBody SolicitudDTO solicitudDTO) {
        Solicitud nueva = solicitudService.crear(SolicitudMapper.fromDTO(solicitudDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudMapper.toDTO(nueva));
    }

    @Operation(
        summary = "Obtener seguimiento de solicitud",
        description = "Consulta el estado y progreso de una solicitud (disponible para CLIENTE)"
    )
    @ApiResponse(responseCode = "200", description = "Seguimiento obtenido")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<?> obtenerSeguimiento(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        return solicitudService.obtenerSeguimiento(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Asignar ruta a solicitud",
        description = "Asigna una ruta existente a una solicitud (solo OPERADOR)"
    )
    @ApiResponse(responseCode = "200", description = "Ruta asignada exitosamente")
    @ApiResponse(responseCode = "404", description = "Solicitud o ruta no encontrada")
    @PutMapping("/{id}/ruta/{rutaId}")
    public ResponseEntity<SolicitudDTO> asignarRuta(
            @Parameter(description = "ID de la solicitud") @PathVariable("id") Long solicitudId,
            @Parameter(description = "ID de la ruta") @PathVariable("rutaId") Long rutaId) {
        Solicitud actualizada = solicitudService.asignarRuta(solicitudId, rutaId);
        return ResponseEntity.ok(SolicitudMapper.toDTO(actualizada));
    }

    @Operation(
        summary = "Eliminar solicitud",
        description = "Elimina una solicitud del sistema (solo OPERADOR)"
    )
    @ApiResponse(responseCode = "204", description = "Solicitud eliminada")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}