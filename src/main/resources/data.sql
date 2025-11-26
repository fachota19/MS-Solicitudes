-- 1. TIPOS DE ESTADO (Forzando IDs)
INSERT INTO tipos_estado (id, nombre) VALUES (1, 'PENDIENTE') ON CONFLICT (id) DO NOTHING;
INSERT INTO tipos_estado (id, nombre) VALUES (2, 'EN_PROCESO') ON CONFLICT (id) DO NOTHING;
INSERT INTO tipos_estado (id, nombre) VALUES (3, 'COMPLETADO') ON CONFLICT (id) DO NOTHING;
-- Actualizar secuencia
SELECT setval('tipos_estado_id_seq', (SELECT MAX(id) FROM tipos_estado));

-- 2. TIPOS DE TRAMO (Forzando IDs)
INSERT INTO tipos_tramo (id, nombre) VALUES (1, 'DEPÓSITO') ON CONFLICT (id) DO NOTHING;
INSERT INTO tipos_tramo (id, nombre) VALUES (2, 'TRASLADO') ON CONFLICT (id) DO NOTHING;
INSERT INTO tipos_tramo (id, nombre) VALUES (3, 'ENTREGA') ON CONFLICT (id) DO NOTHING;
-- Actualizar secuencia
SELECT setval('tipos_tramo_id_seq', (SELECT MAX(id) FROM tipos_tramo));

-- 3. CONTENEDORES
INSERT INTO contenedores (id, peso_kg, volumen_m3) VALUES (1, 2500, 12.5) ON CONFLICT (id) DO NOTHING;
SELECT setval('contenedores_id_seq', (SELECT MAX(id) FROM contenedores));

-- 4. RUTAS
INSERT INTO rutas (id) VALUES (1) ON CONFLICT (id) DO NOTHING;
SELECT setval('rutas_id_seq', (SELECT MAX(id) FROM rutas));

-- 5. SOLICITUD BASE (Coincide con tus entidades: estado, contenedor_id, etc.)
INSERT INTO solicitudes (
    id, numero_seguimiento, estado, cliente_id, tarifa_id, 
    contenedor_id, camion_id, fecha_creacion, costo_estimado, 
    origen_direccion, destino_direccion, ruta_id
) VALUES (
    1, 'SOL-TEST', 1, 1, 1, 
    1, 3, CURRENT_DATE, 18500.75, 
    'Depósito Central', 'Puerto Rosario', 1
) ON CONFLICT (id) DO NOTHING;
SELECT setval('solicitudes_id_seq', (SELECT MAX(id) FROM solicitudes));

-- 6. TRAMOS (Ahora sí encontrará el estado 1 y tipo_tramo 1)
INSERT INTO tramos (
    id, orden, estado, tipo_tramo, ruta_id, 
    distancia_estimada_km, costo_estimado
) VALUES (1, 1, 1, 1, 1, 120.5, 5000) ON CONFLICT (id) DO NOTHING;

INSERT INTO tramos (
    id, orden, estado, tipo_tramo, ruta_id, 
    distancia_estimada_km, costo_estimado
) VALUES (2, 2, 1, 2, 1, 350.0, 13500) ON CONFLICT (id) DO NOTHING;

SELECT setval('tramos_id_seq', (SELECT MAX(id) FROM tramos));