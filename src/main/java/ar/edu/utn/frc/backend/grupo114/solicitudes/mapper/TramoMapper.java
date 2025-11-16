package ar.edu.utn.frc.backend.grupo114.solicitudes.mapper;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.TramoDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Tramo;

public class TramoMapper {

    public static TramoDTO toDTO(Tramo entity) {
        if (entity == null) {
            return null;
        }
        
        TramoDTO dto = new TramoDTO();
        dto.setId(entity.getId());
        dto.setOrden(entity.getOrden());
        dto.setEstadoId(entity.getEstado() != null ? entity.getEstado().getId() : null);
        dto.setDistanciaEstimadaKm(entity.getDistanciaEstimadaKm());
        dto.setCostoEstimado(entity.getCostoEstimado());
        dto.setFechaHoraInicioEstimada(entity.getFechaHoraInicioEstimada());
        dto.setFechaHoraFinEstimada(entity.getFechaHoraFinEstimada());
        dto.setTipoTramoId(entity.getTipoTramo() != null ? entity.getTipoTramo().getId() : null);
        dto.setFechaHoraInicioReal(entity.getFechaHoraInicioReal());
        dto.setFechaHoraFinReal(entity.getFechaHoraFinReal());
        dto.setCostoReal(entity.getCostoReal());
        dto.setRutaId(entity.getRuta() != null ? entity.getRuta().getId() : null);
        dto.setCamionId(entity.getCamionId());
        dto.setOrigenDepositoId(entity.getOrigenDepositoId());
        dto.setDestinoDepositoId(entity.getDestinoDepositoId());
        
        return dto;
    }
}