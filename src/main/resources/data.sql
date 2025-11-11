-- ===========================================
-- Inicialización de datos base
-- ===========================================

-- TIPOS DE ESTADO
INSERT INTO tipos_estado (nombre) VALUES ('PENDIENTE');
INSERT INTO tipos_estado (nombre) VALUES ('EN_PROCESO');
INSERT INTO tipos_estado (nombre) VALUES ('COMPLETADO');

-- TIPOS DE TRAMO
INSERT INTO tipos_tramo (nombre) VALUES ('DEPÓSITO');
INSERT INTO tipos_tramo (nombre) VALUES ('TRASLADO');
INSERT INTO tipos_tramo (nombre) VALUES ('ENTREGA');

-- CONTENEDORES
INSERT INTO contenedores (peso_kg, volumen_m3) VALUES (2500, 12.5);

-- RUTA ASOCIADA (se crea primero)
INSERT INTO rutas (id) VALUES (1);

-- SOLICITUD BASE (ahora con ruta_id)
INSERT INTO solicitudes (
    numero_seguimiento,
    estado,
    cliente_id,
    tarifa_id,
    contenedor_id,
    camion_id,
    fecha_creacion,
    costo_estimado,
    origen_direccion,
    destino_direccion,
    ruta_id
)
VALUES (
    'SOL-TEST',
    1,
    1,
    1,
    1,
    3,
    CURRENT_DATE,
    18500.75,
    'Depósito Central Córdoba',
    'Puerto Rosario',
    1
);

-- TRAMOS DE ESA RUTA
INSERT INTO tramos (
    orden,
    estado,
    tipo_tramo,
    ruta_id,
    distancia_estimada_km,
    costo_estimado
)
VALUES (1, 1, 1, 1, 120.5, 5000);

INSERT INTO tramos (
    orden,
    estado,
    tipo_tramo,
    ruta_id,
    distancia_estimada_km,
    costo_estimado
)
VALUES (2, 1, 2, 1, 350.0, 13500);