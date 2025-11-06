package ar.edu.utn.frc.backend.grupo114.solicitudes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Entidad que representa una Solicitud de traslado en el sistema.
 * Cada solicitud pertenece a un cliente, puede involucrar camiones y tarifas,
 * y pasa por distintos estados a lo largo de su ciclo de vida.
 */
@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha en la que se crea la solicitud.
     */
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    /**
     * Estado actual de la solicitud (PENDIENTE, EN_PROCESO, FINALIZADA, CANCELADA, etc.)
     */
    @Column(nullable = false, length = 30)
    private String estado;

    /**
     * Costo estimado de la solicitud, calculado según las tarifas vigentes.
     */
    @Column(nullable = false)
    private Double costoEstimado;

    /**
     * Identificador del cliente o usuario que originó la solicitud.
     * (más adelante se puede relacionar con el microservicio de Usuarios)
     */
    private Long clienteId;

    /**
     * Identificador de la tarifa usada para calcular el costo.
     * (más adelante se relacionará con ms-tarifas)
     */
    private Long tarifaId;

    /**
     * Identificador del camión asignado.
     * (más adelante se relacionará con ms-camiones)
     */
    private Long camionId;
}
