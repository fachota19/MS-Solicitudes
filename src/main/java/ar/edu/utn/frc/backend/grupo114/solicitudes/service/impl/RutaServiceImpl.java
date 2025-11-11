package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.RutaRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.SolicitudRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.RutaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public Ruta obtenerPorSolicitud(Long solicitudId) {
        Ruta ruta = rutaRepository.findBySolicitudId(solicitudId);
        if (ruta == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró ruta para la solicitud " + solicitudId);
        return ruta;
    }
}
