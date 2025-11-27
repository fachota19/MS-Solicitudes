package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.client.TarifasClient;
import ar.edu.utn.frc.backend.grupo114.solicitudes.client.UsuariosClient;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.*;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final TipoEstadoRepository tipoEstadoRepository;
    private final RutaRepository rutaRepository;
    private final UsuariosClient usuariosClient;
    private final TarifasClient tarifasClient;

    @Override
    public List<Solicitud> listarTodas() {
        log.info("Listando todas las solicitudes");
        return solicitudRepository.findAll();
    }

    @Override
    public Optional<Solicitud> obtenerPorId(Long id) {
        log.info("Buscando solicitud con ID: {}", id);
        return solicitudRepository.findById(id);
    }

    @Override
    public Solicitud crear(Solicitud solicitud) {
        validarCliente(solicitud.getClienteId());

        Double costoEstimado = calcularCostoEstimado(solicitud);
        solicitud.setCostoEstimado(costoEstimado);

        solicitud.setNumeroSeguimiento("SOL-" + System.currentTimeMillis());

        TipoEstado estadoPendiente = tipoEstadoRepository.findByNombre("PENDIENTE");
        solicitud.setEstado(estadoPendiente);

        return solicitudRepository.save(solicitud);
    }

    private void validarCliente(Long clienteId) {
        try {
            usuariosClient.obtenerCliente(clienteId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cliente no válido");
        }
    }

    private Double calcularCostoEstimado(Solicitud solicitud) {
        try {
            TarifasClient.CostoRequest request = new TarifasClient.CostoRequest(
                    100.0,
                    solicitud.getContenedor().getPesoKg(),
                    solicitud.getContenedor().getVolumenM3(),
                    0,
                    solicitud.getCamionId()
            );

            TarifasClient.CostoResponse response = tarifasClient.calcularCosto(request);
            return response.costoTotal();

        } catch (Exception e) {
            log.warn("Error al calcular costo estimado: {}", e.getMessage());
            return 0.0;
        }
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando solicitud con ID: {}", id);

        if (!solicitudRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontró la solicitud con ID: " + id);
        }

        solicitudRepository.deleteById(id);
        log.info("Solicitud eliminada");
    }

    @Override
    public Optional<Map<String, Object>> obtenerSeguimiento(Long id) {
        return solicitudRepository.findById(id).map(this::mapSeguimiento);
    }

    @Override
    public Optional<Map<String, Object>> obtenerSeguimientoPorNumero(String numeroSeguimiento) {
        Solicitud solicitud = solicitudRepository.findByNumeroSeguimiento(numeroSeguimiento);
        return Optional.ofNullable(solicitud).map(this::mapSeguimiento);
    }

    @Override
    public Solicitud asignarRuta(Long solicitudId, Long rutaId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Solicitud no encontrada"));

        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ruta no encontrada"));

        solicitud.setRuta(ruta);
        return solicitudRepository.save(solicitud);
    }

    @Override
    public Solicitud actualizar(Long id, Solicitud solicitud) {
        if (!solicitudRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Solicitud no encontrada");
        }

        solicitud.setId(id);
        return solicitudRepository.save(solicitud);
    }

    private Map<String, Object> mapSeguimiento(Solicitud s) {
        Map<String, Object> seg = new LinkedHashMap<>();
        seg.put("id", s.getId());
        seg.put("numeroSeguimiento", s.getNumeroSeguimiento());
        seg.put("estado", s.getEstado().getNombre());
        seg.put("origen", s.getOrigenDireccion());
        seg.put("destino", s.getDestinoDireccion());
        seg.put("fechaCreacion", s.getFechaCreacion());
        seg.put("costoEstimado", s.getCostoEstimado());

        if (s.getRuta() != null && s.getRuta().getTramos() != null) {
            seg.put("rutaId", s.getRuta().getId());
            seg.put("cantidadTramos", s.getRuta().getTramos().size());
        } else {
            seg.put("rutaId", null);
            seg.put("cantidadTramos", 0);
        }
        return seg;
    }
}
