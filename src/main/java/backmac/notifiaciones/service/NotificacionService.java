package backmac.notifiaciones.service;

import backmac.notifiaciones.dto.EnviarNotificacionDTO;
import backmac.notifiaciones.entity.Notificacion;
import backmac.notifiaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Transactional
    public Notificacion enviar(EnviarNotificacionDTO dto) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setPedidoId(dto.getPedidoId());
        notificacion.setTipo(dto.getTipo());
        notificacion.setMensaje(dto.getMensaje());

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("[NOTIFICACION] Tipo: {} | Usuario: {} | Pedido: {} | Mensaje: {}",
                dto.getTipo(), dto.getUsuarioId(), dto.getPedidoId(), dto.getMensaje());
        return guardada;
    }

    public List<Notificacion> obtenerMisNotificaciones(String usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    public List<Notificacion> obtenerNoLeidas(String usuarioId) {
        return notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(usuarioId);
    }

    public long contarNoLeidas(String usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeidaFalse(usuarioId);
    }

    @Transactional
    public Notificacion marcarComoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificacion no encontrada: " + id));
        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }
}
