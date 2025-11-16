package ar.edu.utn.frc.backend.grupo114.solicitudes.mapper;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.RutaDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import java.util.stream.Collectors;

public class RutaMapper {

    public static RutaDTO toDTO(Ruta entity) {
        if (entity == null) {
            return null;
        }
        
        RutaDTO dto = new RutaDTO();
        dto.setId(entity.getId());
        dto.setSolicitudId(entity.getSolicitud() != null ? entity.getSolicitud().getId() : null);
        
        // Mapear los tramos (si existen)
        if (entity.getTramos() != null && !entity.getTramos().isEmpty()) {
            dto.setTramos(
                entity.getTramos().stream()
                    .map(TramoMapper::toDTO)
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
    }
}