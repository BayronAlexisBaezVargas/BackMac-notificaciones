package backmac.notifiaciones.controller;

import backmac.notifiaciones.dto.EnviarNotificacionDTO;
import backmac.notifiaciones.entity.Notificacion;
import backmac.notifiaciones.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    // Endpoint interno: lo llaman otros microservicios (ms-pagos, ms-pedidos)
    // Tambien protegido con token JWT
    @PostMapping
    public ResponseEntity<Notificacion> enviar(@Valid @RequestBody EnviarNotificacionDTO dto,
                                               @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(notificacionService.enviar(dto));
    }

    // El usuario consulta sus propias notificaciones
    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerMisNotificaciones(@AuthenticationPrincipal Jwt jwt) {
        String usuarioId = jwt.getSubject();
        return ResponseEntity.ok(notificacionService.obtenerMisNotificaciones(usuarioId));
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<List<Notificacion>> obtenerNoLeidas(@AuthenticationPrincipal Jwt jwt) {
        String usuarioId = jwt.getSubject();
        return ResponseEntity.ok(notificacionService.obtenerNoLeidas(usuarioId));
    }

    @GetMapping("/contador")
    public ResponseEntity<Map<String, Long>> contarNoLeidas(@AuthenticationPrincipal Jwt jwt) {
        String usuarioId = jwt.getSubject();
        long count = notificacionService.contarNoLeidas(usuarioId);
        return ResponseEntity.ok(Map.of("noLeidas", count));
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }
}
