package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoEstado;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.TipoEstadoRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TipoEstadoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TipoEstadoServiceImpl implements TipoEstadoService {

    private final TipoEstadoRepository repository;

    public TipoEstadoServiceImpl(TipoEstadoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TipoEstado> listarTodos() {
        return repository.findAll();
    }

    @Override
    public TipoEstado obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }
}
