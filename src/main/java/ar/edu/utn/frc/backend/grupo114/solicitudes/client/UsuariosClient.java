package ar.edu.utn.frc.backend.grupo114.solicitudes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para comunicarse con el microservicio de Usuarios.
 */
@FeignClient(
    name = "ms-usuarios",
    url = "${ms.usuarios.url:http://localhost:8082}"
)
public interface UsuariosClient {

    @GetMapping("/api/clientes/{id}")
    ClienteResponse obtenerCliente(@PathVariable Long id);
    
    // DTO de respuesta
    record ClienteResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono
    ) {}
}