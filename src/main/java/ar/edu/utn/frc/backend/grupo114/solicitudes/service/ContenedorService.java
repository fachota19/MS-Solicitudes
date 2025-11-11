package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import java.util.List;

public interface ContenedorService {
    List<Contenedor> listarTodos();
    Contenedor obtenerPorId(Long id);
    Contenedor crear(Contenedor contenedor);
    void eliminar(Long id);
}
