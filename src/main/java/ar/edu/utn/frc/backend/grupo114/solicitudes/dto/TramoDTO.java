package ar.edu.utn.frc.backend.grupo114.solicitudes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TramoDTO {
    private Long id;
    private Integer orden;
    private Long estadoId;
    private Double distanciaEstimadaKm;
    private Double costoEstimado;
    private LocalDateTime fechaHoraInicioEstimada;
    private LocalDateTime fechaHoraFinEstimada;
    private Long tipoTramoId;
    private LocalDateTime fechaHoraInicioReal;
    private LocalDateTime fechaHoraFinReal;
    private Double costoReal;
    private Long rutaId;
    private Long camionId;
    private Long origenDepositoId;
    private Long destinoDepositoId;
}