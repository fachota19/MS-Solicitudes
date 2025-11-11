package ar.edu.utn.frc.backend.grupo114.solicitudes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Cliente Feign para comunicarse con el microservicio de Camiones.
 */
@FeignClient(
    name = "ms-camiones",
    url = "${ms.camiones.url:http://localhost:8084}"
)
public interface CamionesClient {

    @GetMapping("/api/camiones/disponibles")
    List<CamionResponse> obtenerCamionesDisponibles();
    
    @GetMapping("/api/camiones/{id}")
    CamionResponse obtenerCamion(@PathVariable Long id);
    
    // DTO de respuesta
    record CamionResponse(
        Long id,
        String patente,
        Boolean disponible,
        Double consumoCombustibleLKm,
        Double costoPorKm,
        Long idTransportista,
        Long tipoCamionId
    ) {}
}