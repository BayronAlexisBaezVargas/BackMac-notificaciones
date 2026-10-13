package backmac.notifiaciones.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BindingAdminDTO {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre de la cola solo puede contener caracteres alfanuméricos, puntos, guiones y guiones bajos")
    private String queue;

    @NotBlank(message = "El nombre del exchange es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre del exchange solo puede contener caracteres alfanuméricos, puntos, guiones y guiones bajos")
    private String exchange;

    @NotBlank(message = "El routing key es obligatorio")
    private String routingKey;
}
