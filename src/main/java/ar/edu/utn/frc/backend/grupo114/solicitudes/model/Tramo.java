package ar.edu.utn.frc.backend.grupo114.solicitudes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "tramos")
@Getter
@Setter
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer orden;

    @ManyToOne
    @JoinColumn(name = "estado", nullable = false)
    private TipoEstado estado;

    @Column(name = "distancia_estimada_km")
    private Double distanciaEstimadaKm;

    @Column(name = "costo_estimado")
    private Double costoEstimado;

    @Column(name = "fecha_hora_inicio_estimada")
    private LocalDateTime fechaHoraInicioEstimada;

    @Column(name = "fecha_hora_fin_estimada")
    private LocalDateTime fechaHoraFinEstimada;

    @ManyToOne
    @JoinColumn(name = "tipo_tramo", nullable = false)
    private TipoTramo tipoTramo;

    @Column(name = "fecha_hora_inicio_real")
    private LocalDateTime fechaHoraInicioReal;

    @Column(name = "fecha_hora_fin_real")
    private LocalDateTime fechaHoraFinReal;

    @Column(name = "costo_real")
    private Double costoReal;

    @ManyToOne
    @JoinColumn(name = "ruta_id")
    @JsonBackReference  // NO se serializa para evitar loop infinito
    private Ruta ruta;

    @Column(name = "camion_id")
    private Long camionId;

    @Column(name = "origen_deposito_id")
    private Long origenDepositoId;

    @Column(name = "destino_deposito_id")
    private Long destinoDepositoId;
}