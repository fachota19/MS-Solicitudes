package ar.edu.utn.frc.backend.grupo114.solicitudes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    
    private Long id;
    
    private String numeroSeguimiento;
    
    @NotNull(message = "La fecha de creación es obligatoria")
    private LocalDate fechaCreacion;
    
    @NotBlank(message = "La dirección de origen es obligatoria")
    private String origenDireccion;
    
    @NotBlank(message = "La dirección de destino es obligatoria")
    private String destinoDireccion;
    
    @Positive(message = "El costo estimado debe ser positivo")
    private Double costoEstimado;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser positivo")
    private Long clienteId;
    
    @NotNull(message = "El ID de la tarifa es obligatorio")
    @Positive(message = "El ID de la tarifa debe ser positivo")
    private Long tarifaId;
    
    private Long camionId;
    
    @NotNull(message = "El ID del contenedor es obligatorio")
    @Positive(message = "El ID del contenedor debe ser positivo")
    private Long contenedorId;
    
    @NotNull(message = "El ID del estado es obligatorio")
    @Positive(message = "El ID del estado debe ser positivo")
    private Long estadoId;
    
    private Long rutaId;
}