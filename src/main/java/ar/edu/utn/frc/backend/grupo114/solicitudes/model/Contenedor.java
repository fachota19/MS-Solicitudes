package ar.edu.utn.frc.backend.grupo114.solicitudes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contenedores")
@Getter @Setter
public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "peso_kg", nullable = false)
    private Double pesoKg;

    @Column(name = "volumen_m3", nullable = false)
    private Double volumenM3;
}
