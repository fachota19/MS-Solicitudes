package ar.edu.utn.frc.backend.grupo114.solicitudes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa el tipo de tramo dentro de una ruta.
 * Ej: ENTRE_DEPÓSITOS, DEPOSITO_CLIENTE, CLIENTE_DEPÓSITO
 */
@Entity
@Table(name = "tipos_tramo")
@Getter @Setter
public class TipoTramo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)  // ✅ AGREGAR unique = true
    private String nombre;
}
