package ar.edu.utn.frc.backend.grupo114.solicitudes.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_seguimiento", length = 20)
    private String numeroSeguimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado", nullable = false)
    @JsonIgnoreProperties("solicitudes")
    private TipoEstado estado;

    @Column(name = "origen_direccion")
    private String origenDireccion;

    @Column(name = "origen_latitud")
    private Double origenLatitud;

    @Column(name = "origen_longitud")
    private Double origenLongitud;

    @Column(name = "destino_direccion")
    private String destinoDireccion;

    @Column(name = "destino_latitud")
    private Double destinoLatitud;

    @Column(name = "destino_longitud")
    private Double destinoLongitud;

    @Column(name = "costo_estimado")
    private Double costoEstimado;

    @Column(name = "tiempo_estimado_hs")
    private Integer tiempoEstimadoHs;

    @Column(name = "costo_real")
    private Double costoReal;

    @Column(name = "tiempo_real_hs")
    private Integer tiempoRealHs;

    @Column(name = "tarifa_id", nullable = false)
    private Long tarifaId;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contenedor_id", nullable = false)
    @JsonIgnoreProperties("solicitudes")
    private Contenedor contenedor;

    @Column(name = "camion_id")
    private Long camionId;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @OneToOne
    @JoinColumn(name = "ruta_id")
    @JsonManagedReference  // Se serializa normalmente
    private Ruta ruta;
}