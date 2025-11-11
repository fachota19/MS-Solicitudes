package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;

public interface RutaService {
    Ruta asignarRuta(Long solicitudId, Ruta ruta);
    Ruta obtenerPorSolicitud(Long solicitudId);
}
