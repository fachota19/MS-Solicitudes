package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoTramo;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.TipoTramoRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TipoTramoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TipoTramoServiceImpl implements TipoTramoService {

    private final TipoTramoRepository repository;

    public TipoTramoServiceImpl(TipoTramoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TipoTramo> listarTodos() {
        return repository.findAll();
    }

    @Override
    public TipoTramo obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }
}
