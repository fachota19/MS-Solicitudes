package ar.edu.utn.frc.backend.grupo114.solicitudes.controller;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.CrearSolicitudRequest;
import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.SolicitudDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.mapper.SolicitudMapper;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoEstado;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.RutaRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.ContenedorService;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.SolicitudService;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TipoEstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de transporte de contenedores")
@SecurityRequirement(name = "bearerAuth")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final ContenedorService contenedorService;
    private final TipoEstadoService tipoEstadoService;
    private final RutaRepository rutaRepository;  // ✅ AGREGADO

    // ✅ CONSTRUCTOR ACTUALIZADO
    public SolicitudController(
            SolicitudService solicitudService,
            ContenedorService contenedorService,
            TipoEstadoService tipoEstadoService,
            RutaRepository rutaRepository
    ) {
        this.solicitudService = solicitudService;
        this.contenedorService = contenedorService;
        this.tipoEstadoService = tipoEstadoService;
        this.rutaRepository = rutaRepository;
    }

    @Operation(
        summary = "Listar todas las solicitudes",
        description = "Obtiene todas las solicitudes de transporte (solo OPERADOR)"
    )
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> listarTodas() {
        List<SolicitudDTO> dtos = solicitudService.listarTodas()
            .stream()
            .map(SolicitudMapper::toDTO)
            .toList();
        return ResponseEntity.ok(dtos);
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
    public ResponseEntity<SolicitudDTO> crear(@Valid @RequestBody CrearSolicitudRequest request) {
        // 1️⃣ Crear el contenedor
        Contenedor contenedor = new Contenedor();
        contenedor.setPesoKg(request.getContenedor().getPesoKg());
        contenedor.setVolumenM3(request.getContenedor().getVolumenM3());
        Contenedor contenedorGuardado = contenedorService.crear(contenedor);
        
        // 2️⃣ Crear la solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setClienteId(request.getClienteId());
        solicitud.setContenedor(contenedorGuardado);
        solicitud.setOrigenDireccion(request.getOrigenDireccion());
        solicitud.setDestinoDireccion(request.getDestinoDireccion());
        solicitud.setFechaCreacion(LocalDate.now());
        solicitud.setTarifaId(1L);  // Tarifa por defecto
        
        // 3️⃣ Asignar estado PENDIENTE
        TipoEstado estadoPendiente = tipoEstadoService.obtenerPorNombre("PENDIENTE");
        if (estadoPendiente == null) {
            throw new IllegalStateException("No se encontró el estado PENDIENTE en la base de datos");
        }
        solicitud.setEstado(estadoPendiente);
        
        // 4️⃣ Guardar solicitud
        Solicitud nueva = solicitudService.crear(solicitud);
        
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
        summary = "Crear y asignar ruta a solicitud",
        description = "Crea una nueva ruta vacía y la asigna a una solicitud (solo OPERADOR)"
    )
    @ApiResponse(responseCode = "200", description = "Ruta creada y asignada exitosamente")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    @ApiResponse(responseCode = "400", description = "La solicitud ya tiene una ruta asignada")
    @PostMapping("/{id}/ruta")  // ✅ CAMBIADO: POST sin {rutaId}
    public ResponseEntity<SolicitudDTO> crearYAsignarRuta(
            @Parameter(description = "ID de la solicitud") @PathVariable("id") Long solicitudId) {
        
        // 1️⃣ Buscar la solicitud
        Solicitud solicitud = solicitudService.obtenerPorId(solicitudId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Solicitud no encontrada con ID: " + solicitudId));
        
        // 2️⃣ Validar que no tenga ya una ruta asignada
        if (solicitud.getRuta() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "La solicitud ya tiene una ruta asignada");
        }
        
        // 3️⃣ Crear nueva ruta vacía
        Ruta nuevaRuta = Ruta.builder()
            .solicitud(solicitud)
            .build();
        
        // 4️⃣ Guardar la ruta
        Ruta rutaGuardada = rutaRepository.save(nuevaRuta);
        
        // 5️⃣ Asignar la ruta a la solicitud
        solicitud.setRuta(rutaGuardada);
        Solicitud actualizada = solicitudService.actualizar(solicitudId, solicitud);
        
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