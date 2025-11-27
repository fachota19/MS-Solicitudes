package ar.edu.utn.frc.backend.grupo114.solicitudes.service.impl;

import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Solicitud;
import ar.edu.utn.frc.backend.grupo114.solicitudes.model.Tramo;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.SolicitudRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.TipoEstadoRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.repository.TramoRepository;
import ar.edu.utn.frc.backend.grupo114.solicitudes.service.TramoService;
import ar.edu.utn.frc.backend.grupo114.solicitudes.client.CamionesClient; // Importación necesaria
import ar.edu.utn.frc.backend.grupo114.solicitudes.client.TarifasClient; // Importación necesaria
import ar.edu.utn.frc.backend.grupo114.solicitudes.client.TarifasClient.CostoRequest; // Importación necesaria
import ar.edu.utn.frc.backend.grupo114.solicitudes.client.TarifasClient.CostoResponse; // Importación necesaria

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.time.temporal.ChronoUnit; // Importación necesaria

@Service
@Transactional
public class TramoServiceImpl implements TramoService {

    private final TramoRepository tramoRepository;
    private final TipoEstadoRepository tipoEstadoRepository;
    private final SolicitudRepository solicitudRepository;
    private final CamionesClient camionesClient; // Declaración de campo
    private final TarifasClient tarifasClient;   // Declaración de campo

    public TramoServiceImpl(TramoRepository tramoRepository,
                            TipoEstadoRepository tipoEstadoRepository,
                            SolicitudRepository solicitudRepository,
                            CamionesClient camionesClient,             // Inyección
                            TarifasClient tarifasClient) {             // Inyección
        this.tramoRepository = tramoRepository;
        this.tipoEstadoRepository = tipoEstadoRepository;
        this.solicitudRepository = solicitudRepository;
        this.camionesClient = camionesClient;
        this.tarifasClient = tarifasClient;
    }

    /**
     * Requerimientos 8 y 9: Calcula el costo total real y el tiempo real
     * y los registra en la solicitud.
     */
    private void calcularYFinalizarSolicitud(Solicitud solicitud) {
        double totalDistanciaKm = 0.0;
        long totalTiempoRealHoras = 0L;

        // Iterar sobre todos los tramos de la ruta
        for (Tramo t : solicitud.getRuta().getTramos()) {
            // 1. Recorrido total
            totalDistanciaKm += t.getDistanciaEstimadaKm();

            // 2. Tiempo real de traslado
            if (t.getFechaHoraInicioReal() != null && t.getFechaHoraFinReal() != null) {
                // Calcular diferencia en horas
                long duracionHoras = ChronoUnit.HOURS.between(t.getFechaHoraInicioReal(), t.getFechaHoraFinReal());
                totalTiempoRealHoras += duracionHoras;
            }
        }

        // 3. Estadía en depósitos (días de estadía).
        // NOTA: La lógica para determinar la estadía real es compleja (requiere comparar fechas
        // de fin de un tramo tipo DEPÓSITO con inicio del siguiente tramo tipo DEPÓSITO).
        int diasEstadia = 0; // Se asume 0 o se debe implementar lógica de cálculo aquí.

        // 4. Peso y volumen (del contenedor asociado)
        double pesoKg = solicitud.getContenedor().getPesoKg();
        double volumenM3 = solicitud.getContenedor().getVolumenM3();

        // 5. ID del camión
        Long camionId = solicitud.getCamionId();

        // Llamar al MS-Tarifas para calcular el Costo Real
        try {
            CostoRequest request = new CostoRequest(
                totalDistanciaKm,
                pesoKg,
                volumenM3,
                diasEstadia,
                camionId
            );

            CostoResponse response = tarifasClient.calcularCosto(request);

            // Registrar el cálculo de tiempo real y costo real en la solicitud (Requerimiento 9)
            solicitud.setCostoReal(response.costoTotal());
            solicitud.setTiempoRealHs((int) totalTiempoRealHoras);

            // Persistir los cambios finales de la solicitud
            solicitudRepository.save(solicitud);

        } catch (Exception e) {
            // Manejo de errores de la comunicación con el microservicio de Tarifas
            System.err.println("Error FATAL al calcular costo real y tiempo real para solicitud " + solicitud.getId() + ": " + e.getMessage());
            // Se puede considerar lanzar una excepción para que el Transportista reciba un error 500
            // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al calcular costo final: " + e.getMessage());
        }
    }


    @Override
    public Optional<Tramo> iniciarTramo(Long id, LocalDateTime fechaHoraInicioReal) {
        return tramoRepository.findById(id).map(tramo -> {

            // 1. Validar estado actual del tramo
            if (!"PENDIENTE".equals(tramo.getEstado().getNombre())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El tramo debe estar en estado PENDIENTE para iniciarlo");
            }

            // 2. Actualizar Tramo
            tramo.setFechaHoraInicioReal(fechaHoraInicioReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("EN_PROCESO"));
            Tramo tramoGuardado = tramoRepository.save(tramo);

            // 3. Actualizar Solicitud (Cascada): Si empieza el viaje, Solicitud -> EN_TRANSITO
            if (tramo.getRuta() != null && tramo.getRuta().getSolicitud() != null) {
                Solicitud solicitud = tramo.getRuta().getSolicitud();

                // Solo cambiamos si estaba esperando (Programada)
                if ("PROGRAMADA".equals(solicitud.getEstado().getNombre())) {
                    solicitud.setEstado(tipoEstadoRepository.findByNombre("EN_TRANSITO"));
                    solicitudRepository.save(solicitud);
                }
            }

            return tramoGuardado;
        });
    }

    @Override
    public Optional<Tramo> finalizarTramo(Long id, LocalDateTime fechaHoraFinReal) {
        return tramoRepository.findById(id).map(tramo -> {

            // 1. Validaciones
            if (!"EN_PROCESO".equals(tramo.getEstado().getNombre())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El tramo debe estar EN_PROCESO para finalizarlo");
            }
            if (tramo.getFechaHoraInicioReal() != null && fechaHoraFinReal.isBefore(tramo.getFechaHoraInicioReal())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La fecha de fin no puede ser anterior a la de inicio");
            }

            // 2. Actualizar Tramo
            tramo.setFechaHoraFinReal(fechaHoraFinReal);
            tramo.setEstado(tipoEstadoRepository.findByNombre("COMPLETADO"));
            Tramo tramoGuardado = tramoRepository.save(tramo);

            // 3. Actualizar Solicitud (Cascada): Verificar si se completó todo el viaje
            if (tramo.getRuta() != null && tramo.getRuta().getSolicitud() != null) {
                Solicitud solicitud = tramo.getRuta().getSolicitud();

                // Verificar si TODOS los tramos de esta ruta están COMPLETADOS
                // NOTA: Si el modelo de Ruta no trae los Tramos, este stream puede fallar (LazyLoading).
                // Asegúrate de que la relación Ruta <-> Tramos es EAGER o usa un método de repositorio
                // para cargar la ruta con todos sus tramos antes de esta comprobación.
                boolean viajeFinalizado = solicitud.getRuta().getTramos().stream()
                        .allMatch(t -> "COMPLETADO".equals(t.getEstado().getNombre()));

                if (viajeFinalizado) {
                    // Requerimiento 9: Cambiar estado a ENTREGADA y calcular costos/tiempos finales
                    solicitud.setEstado(tipoEstadoRepository.findByNombre("ENTREGADA"));

                    // Llamada al método que implementa R8 y R9
                    calcularYFinalizarSolicitud(solicitud);
                    // Importante: No se necesita `solicitudRepository.save(solicitud)` aquí, ya que
                    // se realiza dentro de `calcularYFinalizarSolicitud`.
                }
            }

            return tramoGuardado;
        });
    }
}