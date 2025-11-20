package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.TramoDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.RutaDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.mapper.RutaMapper;
import ar.edu.utn.frc.backend.grupo114.solicitudes.mapper.TramoMapper;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Tramo;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.RutaRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.SolicitudRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class RutaServiceImpl implements RutaService {

    private final RutaRepository rutaRepository;
    private final SolicitudRepository solicitudRepository;

    public RutaServiceImpl(RutaRepository rutaRepository, SolicitudRepository solicitudRepository) {
        this.rutaRepository = rutaRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public Ruta asignarRuta(Long solicitudId, Ruta ruta) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));
        ruta.setSolicitud(solicitud);
        return rutaRepository.save(ruta);
    }

    @Override
    public RutaDTO generarRutaOptima(Long solicitudId) {
        // por ahora no implementado
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Ruta obtenerPorSolicitud(Long solicitudId) {
        Ruta ruta = rutaRepository.findBySolicitudId(solicitudId);
        if (ruta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontró ruta para la solicitud " + solicitudId);
        }

        // Forzar carga de tramos
        ruta.getTramos().size();

        return ruta;
    }

    @Override
    public Ruta crearRutaConTramos(Long solicitudId, List<TramoDTO> tramos) {
        throw new UnsupportedOperationException("Método crearRutaConTramos aún no implementado");
    }
}