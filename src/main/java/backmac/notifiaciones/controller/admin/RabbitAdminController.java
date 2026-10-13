package backmac.notifiaciones.controller.admin;

import backmac.notifiaciones.dto.admin.BindingAdminDTO;
import backmac.notifiaciones.dto.admin.ExchangeAdminDTO;
import backmac.notifiaciones.dto.admin.QueueAdminDTO;
import backmac.notifiaciones.service.admin.RabbitAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class RabbitAdminController {

    private final RabbitAdminService rabbitAdminService;

    // ==========================================
    // GESTIÓN DE COLAS
    // ==========================================
    @PostMapping("/queues")
    public ResponseEntity<Map<String, String>> crearCola(@Valid @RequestBody QueueAdminDTO dto) {
        rabbitAdminService.crearCola(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Cola '" + dto.getName() + "' creada exitosamente"));
    }

    @DeleteMapping("/queues/{name}")
    public ResponseEntity<Map<String, String>> eliminarCola(@PathVariable("name") String name) {
        boolean eliminada = rabbitAdminService.eliminarCola(name);
        if (eliminada) {
            return ResponseEntity.ok(Map.of("mensaje", "Cola '" + name + "' eliminada exitosamente"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No se encontró o no se pudo eliminar la cola '" + name + "'"));
        }
    }

    // ==========================================
    // GESTIÓN DE EXCHANGES
    // ==========================================
    @PostMapping("/exchanges")
    public ResponseEntity<Map<String, String>> crearExchange(@Valid @RequestBody ExchangeAdminDTO dto) {
        rabbitAdminService.crearExchange(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Exchange '" + dto.getName() + "' creado exitosamente"));
    }

    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<Map<String, String>> eliminarExchange(@PathVariable("name") String name) {
        boolean eliminado = rabbitAdminService.eliminarExchange(name);
        if (eliminado) {
            return ResponseEntity.ok(Map.of("mensaje", "Exchange '" + name + "' eliminado exitosamente"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No se encontró o no se pudo eliminar el exchange '" + name + "'"));
        }
    }

    // ==========================================
    // GESTIÓN DE BINDINGS
    // ==========================================
    @PostMapping("/bindings")
    public ResponseEntity<Map<String, String>> crearBinding(@Valid @RequestBody BindingAdminDTO dto) {
        rabbitAdminService.crearBinding(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Binding creado exitosamente entre cola '" + dto.getQueue() + "' y exchange '" + dto.getExchange() + "'"));
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<Map<String, String>> eliminarBinding(@Valid @RequestBody BindingAdminDTO dto) {
        rabbitAdminService.eliminarBinding(dto);
        return ResponseEntity.ok(Map.of("mensaje", "Binding eliminado exitosamente"));
    }
}
