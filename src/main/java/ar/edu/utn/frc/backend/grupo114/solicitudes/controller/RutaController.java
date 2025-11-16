package ar.edu.utn.frc.backend.grupo114.solicitudes.controller;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.RutaDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.mapper.RutaMapper;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.RutaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rutas")
@CrossOrigin(origins = "*")
@Tag(name = "Rutas", description = "Gestión de rutas de transporte")
@SecurityRequirement(name = "bearerAuth")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @Operation(summary = "Obtener ruta de una solicitud")
    @GetMapping("/solicitud/{id}")
    public ResponseEntity<RutaDTO> obtenerPorSolicitud(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        Ruta ruta = rutaService.obtenerPorSolicitud(id);
        return ResponseEntity.ok(RutaMapper.toDTO(ruta));  // ✅ DEVOLVER DTO
    }
}