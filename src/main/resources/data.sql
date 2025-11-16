-- ===========================================
-- Inicialización de datos base
-- ===========================================

-- TIPOS DE ESTADO (solo inserta si no existe)
INSERT INTO tipos_estado (nombre) 
VALUES ('PENDIENTE')
ON CONFLICT DO NOTHING;

INSERT INTO tipos_estado (nombre) 
VALUES ('EN_PROCESO')
ON CONFLICT DO NOTHING;

INSERT INTO tipos_estado (nombre) 
VALUES ('COMPLETADO')
ON CONFLICT DO NOTHING;

-- TIPOS DE TRAMO
INSERT INTO tipos_tramo (nombre) 
VALUES ('DEPÓSITO')
ON CONFLICT DO NOTHING;

INSERT INTO tipos_tramo (nombre) 
VALUES ('TRASLADO')
ON CONFLICT DO NOTHING;

INSERT INTO tipos_tramo (nombre) 
VALUES ('ENTREGA')
ON CONFLICT DO NOTHING;

-- CONTENEDORES
INSERT INTO contenedores (id, peso_kg, volumen_m3) 
VALUES (1, 2500, 12.5)
ON CONFLICT (id) DO NOTHING;

-- 🔥 ACTUALIZAR SECUENCIA DE CONTENEDORES
SELECT setval('contenedores_id_seq', (SELECT COALESCE(MAX(id), 1) FROM contenedores));

-- RUTA ASOCIADA
INSERT INTO rutas (id) 
VALUES (1)
ON CONFLICT (id) DO NOTHING;

-- 🔥 ACTUALIZAR SECUENCIA DE RUTAS
SELECT setval('rutas_id_seq', (SELECT COALESCE(MAX(id), 1) FROM rutas));

-- SOLICITUD BASE
INSERT INTO solicitudes (
    id,
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
    1,
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
)
ON CONFLICT (id) DO NOTHING;

-- 🔥 ACTUALIZAR SECUENCIA DE SOLICITUDES
SELECT setval('solicitudes_id_seq', (SELECT COALESCE(MAX(id), 1) FROM solicitudes));

-- TRAMOS DE ESA RUTA
INSERT INTO tramos (
    id,
    orden,
    estado,
    tipo_tramo,
    ruta_id,
    distancia_estimada_km,
    costo_estimado
)
VALUES (1, 1, 1, 1, 1, 120.5, 5000)
ON CONFLICT (id) DO NOTHING;

INSERT INTO tramos (
    id,
    orden,
    estado,
    tipo_tramo,
    ruta_id,
    distancia_estimada_km,
    costo_estimado
)
VALUES (2, 2, 1, 2, 1, 350.0, 13500)
ON CONFLICT (id) DO NOTHING;

-- 🔥 ACTUALIZAR SECUENCIA DE TRAMOS
SELECT setval('tramos_id_seq', (SELECT COALESCE(MAX(id), 1) FROM tramos));

-- 🔥 ACTUALIZAR SECUENCIAS DE TIPOS (POR SI ACASO)
SELECT setval('tipos_estado_id_seq', (SELECT COALESCE(MAX(id), 1) FROM tipos_estado));
SELECT setval('tipos_tramo_id_seq', (SELECT COALESCE(MAX(id), 1) FROM tipos_tramo));