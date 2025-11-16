package ar.edu.utn.frc.backend.grupo114.solicitudes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaDTO {
    private Long id;
    private Long solicitudId;
    private List<TramoDTO> tramos;
}