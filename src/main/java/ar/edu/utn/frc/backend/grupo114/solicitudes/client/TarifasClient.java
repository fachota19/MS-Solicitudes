package ar.edu.utn.frc.backend.grupo114.solicitudes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para comunicarse con el microservicio de Tarifas.
 */
@FeignClient(
    name = "ms-tarifas",
    url = "${ms.tarifas.url:http://localhost:8083}"
)
public interface TarifasClient {

    @PostMapping("/api/tarifas/calcular")
    CostoResponse calcularCosto(@RequestBody CostoRequest request);
    
    // DTOs
    record CostoRequest(
        Double distanciaKm,
        Double pesoKg,
        Double volumenM3,
        Integer diasEstadia,
        Long camionId
    ) {}
    
    record CostoResponse(
        Double costoTotal,
        DetalleCosto detalle
    ) {}
    
    record DetalleCosto(
        Double costoKm,
        Double costoPeso,
        Double costoVolumen,
        Double costoEstadia
    ) {}
}