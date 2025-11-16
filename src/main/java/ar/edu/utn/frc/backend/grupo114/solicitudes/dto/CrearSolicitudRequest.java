package ar.edu.utn.frc.backend.grupo114.solicitudes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearSolicitudRequest {
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
    
    @NotNull(message = "El contenedor es obligatorio")
    @Valid
    private ContenedorRequest contenedor;
    
    @NotBlank(message = "La dirección de origen es obligatoria")
    private String origenDireccion;
    
    @NotBlank(message = "La dirección de destino es obligatoria")
    private String destinoDireccion;
    
    // Nested class para el contenedor
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContenedorRequest {
        @NotNull(message = "El peso es obligatorio")
        private Double pesoKg;
        
        @NotNull(message = "El volumen es obligatorio")
        private Double volumenM3;
    }
}