package ar.edu.utn.frc.backend.grupo114.solicitudes.mapper;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.ContenedorDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import java.util.stream.Collectors;

public class ContenedorMapper {
    public static ContenedorDTO toDTO(Contenedor entity) {
        if (entity == null) return null;
        return new ContenedorDTO(
            entity.getId(),
            entity.getPesoKg(),
            entity.getVolumenM3()
        );
    }
}