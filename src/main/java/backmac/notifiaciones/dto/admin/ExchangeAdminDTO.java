package backmac.notifiaciones.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ExchangeAdminDTO {

    @NotBlank(message = "El nombre del exchange no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre del exchange solo puede contener caracteres alfanuméricos, puntos, guiones y guiones bajos")
    private String name;

    @Pattern(regexp = "^(?i)(direct|topic|fanout|headers)$", message = "El tipo de exchange debe ser direct, topic, fanout o headers")
    private String type = "topic";

    private boolean durable = true;
    private boolean autoDelete = false;
}
