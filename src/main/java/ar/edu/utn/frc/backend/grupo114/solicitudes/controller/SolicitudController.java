package ar.edu.utn.frc.backend.grupo114.solicitudes.controller;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.CrearSolicitudRequest;
import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.SolicitudDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.mapper.SolicitudMapper;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoEstado;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.RutaRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.SolicitudRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.ContenedorService;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.SolicitudService;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TipoEstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final RutaRepository rutaRepository;
    private final SolicitudRepository solicitudRepository; // Necesario para el update directo

    public SolicitudController(
            SolicitudService solicitudService,
            ContenedorService contenedorService,
            TipoEstadoService tipoEstadoService,
            RutaRepository rutaRepository,
            SolicitudRepository solicitudRepository
    ) {
        this.solicitudService = solicitudService;
        this.contenedorService = contenedorService;
        this.tipoEstadoService = tipoEstadoService;
        this.rutaRepository = rutaRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @Operation(summary = "Listar todas las solicitudes", description = "Obtiene todas las solicitudes (solo OPERADOR)")
    @ApiResponse(responseCode = "200", description = "Lista obtenida")
    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> listarTodas() {
        List<SolicitudDTO> dtos = solicitudService.listarTodas()
                .stream()
                .map(SolicitudMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener solicitud por ID")
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> obtenerPorId(@Parameter(description = "ID solicitud") @PathVariable Long id) {
        return solicitudService.obtenerPorId(id)
                .map(SolicitudMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nueva solicitud", description = "Cliente crea solicitud (Estado inicial: PENDIENTE)")
    @ApiResponse(responseCode = "201", description = "Creada exitosamente")
    @PostMapping
    public ResponseEntity<SolicitudDTO> crear(@Valid @RequestBody CrearSolicitudRequest request) {
        // 1. Crear contenedor
        Contenedor contenedor = new Contenedor();
        contenedor.setPesoKg(request.getContenedor().getPesoKg());
        contenedor.setVolumenM3(request.getContenedor().getVolumenM3());
        Contenedor contenedorGuardado = contenedorService.crear(contenedor);

        // 2. Configurar solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setClienteId(request.getClienteId());
        solicitud.setContenedor(contenedorGuardado);
        solicitud.setOrigenDireccion(request.getOrigenDireccion());
        solicitud.setDestinoDireccion(request.getDestinoDireccion());
        solicitud.setFechaCreacion(LocalDate.now());
        solicitud.setTarifaId(1L);

        // 3. Asignar estado PENDIENTE
        TipoEstado estadoPendiente = tipoEstadoService.obtenerPorNombre("PENDIENTE");
        if (estadoPendiente == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Estado PENDIENTE no configurado");
        }
        solicitud.setEstado(estadoPendiente);

        // 4. Guardar
        Solicitud nueva = solicitudService.crear(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudMapper.toDTO(nueva));
    }

    @Operation(summary = "Obtener seguimiento")
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<?> obtenerSeguimiento(@PathVariable Long id) {
        return solicitudService.obtenerSeguimiento(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear y asignar ruta", description = "Genera ruta y cambia estado a PROGRAMADA")
    @ApiResponse(responseCode = "200", description = "Ruta asignada, estado actualizado")
    @PostMapping("/{id}/ruta")
    public ResponseEntity<SolicitudDTO> crearYAsignarRuta(@PathVariable("id") Long solicitudId) {

        // 1. Buscar solicitud
        Solicitud solicitud = solicitudService.obtenerPorId(solicitudId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        // 2. Validar que no tenga ruta
        if (solicitud.getRuta() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud ya tiene una ruta asignada");
        }

        // 3. Crear ruta
        Ruta nuevaRuta = Ruta.builder().solicitud(solicitud).build();
        Ruta rutaGuardada = rutaRepository.save(nuevaRuta); //

        // 4. Actualizar estado a PROGRAMADA y vincular ruta
        TipoEstado estadoProgramada = tipoEstadoService.obtenerPorNombre("PROGRAMADA");
        if (estadoProgramada != null) {
            solicitud.setEstado(estadoProgramada);
        }
        solicitud.setRuta(rutaGuardada);

        // 5. Guardar usando REPOSITORY (evita lógica de reset en Service.crear)
        Solicitud actualizada = solicitudRepository.save(solicitud); //

        return ResponseEntity.ok(SolicitudMapper.toDTO(actualizada));
    }

    @Operation(summary = "Eliminar solicitud")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}