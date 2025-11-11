package ar.edu.utn.frc.backend.grupo114.solicitudes.controller;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.ContenedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
@CrossOrigin(origins = "*")
@Tag(name = "Contenedores", description = "Gestión de contenedores de transporte")
@SecurityRequirement(name = "bearerAuth")
public class ContenedorController {

    private final ContenedorService contenedorService;

    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    @Operation(summary = "Listar todos los contenedores", description = "Devuelve todos los contenedores registrados (solo OPERADOR)")
    @GetMapping
    public ResponseEntity<List<Contenedor>> listarTodos() {
        return ResponseEntity.ok(contenedorService.listarTodos());
    }

    @Operation(summary = "Obtener contenedor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Contenedor> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contenedorService.obtenerPorId(id));
    }

    @Operation(summary = "Crear contenedor", description = "Registra un nuevo contenedor (solo OPERADOR)")
    @PostMapping
    public ResponseEntity<Contenedor> crear(@RequestBody Contenedor contenedor) {
        Contenedor nuevo = contenedorService.crear(contenedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Eliminar contenedor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        contenedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}