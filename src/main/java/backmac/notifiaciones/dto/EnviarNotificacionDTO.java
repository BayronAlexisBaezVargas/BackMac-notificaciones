package backmac.notifiaciones.dto;

import backmac.notifiaciones.entity.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnviarNotificacionDTO {

    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El tipo de notificacion es obligatorio")
    private TipoNotificacion tipo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}
