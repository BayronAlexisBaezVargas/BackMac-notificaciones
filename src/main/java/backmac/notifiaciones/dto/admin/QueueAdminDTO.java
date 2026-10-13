package backmac.notifiaciones.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class QueueAdminDTO {

    @NotBlank(message = "El nombre de la cola no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre de la cola solo puede contener caracteres alfanuméricos, puntos, guiones y guiones bajos")
    private String name;

    private boolean durable = true;
    private boolean exclusive = false;
    private boolean autoDelete = false;
}
