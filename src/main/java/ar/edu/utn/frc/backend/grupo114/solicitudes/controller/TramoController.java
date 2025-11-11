package ar.edu.utn.frc.backend.grupo114.solicitudes.controller;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Tramo;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TramoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/tramos")
@CrossOrigin(origins = "*")
@Tag(name = "Tramos", description = "Gestión de tramos de ruta (TRANSPORTISTA)")
@SecurityRequirement(name = "bearerAuth")
public class TramoController {

    private final TramoService tramoService;

    public TramoController(TramoService tramoService) {
        this.tramoService = tramoService;
    }

    @Operation(
        summary = "Iniciar un tramo",
        description = "El transportista marca el inicio de un tramo de ruta"
    )
    @ApiResponse(responseCode = "200", description = "Tramo iniciado exitosamente")
    @ApiResponse(responseCode = "404", description = "Tramo no encontrado")
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Tramo> iniciarTramo(
            @Parameter(description = "ID del tramo") @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String fecha = body.get("fechaHoraInicioReal");
        return tramoService.iniciarTramo(id, LocalDateTime.parse(fecha))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Finalizar un tramo",
        description = "El transportista marca la finalización de un tramo de ruta"
    )
    @ApiResponse(responseCode = "200", description = "Tramo finalizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Tramo no encontrado")
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Tramo> finalizarTramo(
            @Parameter(description = "ID del tramo") @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String fecha = body.get("fechaHoraFinReal");
        return tramoService.finalizarTramo(id, LocalDateTime.parse(fecha))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}