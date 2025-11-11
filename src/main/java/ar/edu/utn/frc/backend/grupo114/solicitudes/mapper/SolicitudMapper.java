package ar.edu.utn.frc.backend.grupo114.solicitudes.mapper;

import ar.edu.utn.frc.backend.grupo114.solicitudes.dto.SolicitudDTO;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Contenedor;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Ruta;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.TipoEstado;

public class SolicitudMapper {

    public static SolicitudDTO toDTO(Solicitud entity) {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(entity.getId());
        dto.setNumeroSeguimiento(entity.getNumeroSeguimiento());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setOrigenDireccion(entity.getOrigenDireccion());
        dto.setDestinoDireccion(entity.getDestinoDireccion());
        dto.setCostoEstimado(entity.getCostoEstimado());
        dto.setClienteId(entity.getClienteId());
        dto.setTarifaId(entity.getTarifaId());
        dto.setCamionId(entity.getCamionId());
        dto.setContenedorId(entity.getContenedor() != null ? entity.getContenedor().getId() : null);
        dto.setEstadoId(entity.getEstado() != null ? entity.getEstado().getId() : null);
        dto.setRutaId(entity.getRuta() != null ? entity.getRuta().getId() : null);
        return dto;
    }

    public static Solicitud fromDTO(SolicitudDTO dto) {
        Solicitud entity = new Solicitud();
        entity.setId(dto.getId());
        entity.setNumeroSeguimiento(dto.getNumeroSeguimiento());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setOrigenDireccion(dto.getOrigenDireccion());
        entity.setDestinoDireccion(dto.getDestinoDireccion());
        entity.setCostoEstimado(dto.getCostoEstimado());
        entity.setClienteId(dto.getClienteId());
        entity.setTarifaId(dto.getTarifaId());
        entity.setCamionId(dto.getCamionId());

        if (dto.getContenedorId() != null) {
            Contenedor contenedor = new Contenedor();
            contenedor.setId(dto.getContenedorId());
            entity.setContenedor(contenedor);
        }

        if (dto.getEstadoId() != null) {
            TipoEstado estado = new TipoEstado();
            estado.setId(dto.getEstadoId());
            entity.setEstado(estado);
        }

        if (dto.getRutaId() != null) {
            Ruta ruta = new Ruta();
            ruta.setId(dto.getRutaId());
            entity.setRuta(ruta);
        }

        return entity;
    }
}
