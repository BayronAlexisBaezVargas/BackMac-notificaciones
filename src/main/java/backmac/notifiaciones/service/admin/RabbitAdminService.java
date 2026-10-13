package backmac.notifiaciones.service.admin;

import backmac.notifiaciones.dto.admin.BindingAdminDTO;
import backmac.notifiaciones.dto.admin.ExchangeAdminDTO;
import backmac.notifiaciones.dto.admin.QueueAdminDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitAdminService {

    private final RabbitAdmin rabbitAdmin;

    public void crearCola(QueueAdminDTO dto) {
        log.info("[RABBIT-ADMIN] Creando cola: {}", dto.getName());
        Queue queue = new Queue(dto.getName(), dto.isDurable(), dto.isExclusive(), dto.isAutoDelete());
        rabbitAdmin.declareQueue(queue);
    }

    public boolean eliminarCola(String nombreCola) {
        log.info("[RABBIT-ADMIN] Eliminando cola: {}", nombreCola);
        return rabbitAdmin.deleteQueue(nombreCola);
    }

    public void crearExchange(ExchangeAdminDTO dto) {
        log.info("[RABBIT-ADMIN] Creando exchange: {} ({})", dto.getName(), dto.getType());
        Exchange exchange;
        String type = dto.getType().toLowerCase();

        switch (type) {
            case "direct":
                exchange = new DirectExchange(dto.getName(), dto.isDurable(), dto.isAutoDelete());
                break;
            case "fanout":
                exchange = new FanoutExchange(dto.getName(), dto.isDurable(), dto.isAutoDelete());
                break;
            case "headers":
                exchange = new HeadersExchange(dto.getName(), dto.isDurable(), dto.isAutoDelete());
                break;
            case "topic":
            default:
                exchange = new TopicExchange(dto.getName(), dto.isDurable(), dto.isAutoDelete());
                break;
        }

        rabbitAdmin.declareExchange(exchange);
    }

    public boolean eliminarExchange(String nombreExchange) {
        log.info("[RABBIT-ADMIN] Eliminando exchange: {}", nombreExchange);
        return rabbitAdmin.deleteExchange(nombreExchange);
    }

    public void crearBinding(BindingAdminDTO dto) {
        log.info("[RABBIT-ADMIN] Creando binding entre cola '{}' y exchange '{}' con routing key '{}'",
                dto.getQueue(), dto.getExchange(), dto.getRoutingKey());
        Binding binding = new Binding(
                dto.getQueue(),
                Binding.DestinationType.QUEUE,
                dto.getExchange(),
                dto.getRoutingKey(),
                null
        );
        rabbitAdmin.declareBinding(binding);
    }

    public void eliminarBinding(BindingAdminDTO dto) {
        log.info("[RABBIT-ADMIN] Eliminando binding entre cola '{}' y exchange '{}'",
                dto.getQueue(), dto.getExchange());
        Binding binding = new Binding(
                dto.getQueue(),
                Binding.DestinationType.QUEUE,
                dto.getExchange(),
                dto.getRoutingKey(),
                null
        );
        rabbitAdmin.removeBinding(binding);
    }
}
