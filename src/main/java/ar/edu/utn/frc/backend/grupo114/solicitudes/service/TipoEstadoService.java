package ar.edu.utn.frc.backend.grupo114.solicitudes.service;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoEstado;
import java.util.List;

public interface TipoEstadoService {
    List<TipoEstado> listarTodos();
    TipoEstado obtenerPorNombre(String nombre);
}
