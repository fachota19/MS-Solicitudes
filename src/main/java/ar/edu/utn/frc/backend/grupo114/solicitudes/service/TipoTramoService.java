package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoTramo;
import java.util.List;

public interface TipoTramoService {
    List<TipoTramo> listarTodos();
    TipoTramo obtenerPorNombre(String nombre);
}
