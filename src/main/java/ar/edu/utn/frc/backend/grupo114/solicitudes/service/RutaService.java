package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.RutaDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.TramoDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;

import java.util.List;

public interface RutaService {

    Ruta asignarRuta(Long solicitudId, Ruta ruta);

    RutaDTO generarRutaOptima(Long solicitudId);

    Ruta crearRutaConTramos(Long solicitudId, List<TramoDTO> tramos);

    Ruta obtenerPorSolicitud(Long solicitudId);
}
