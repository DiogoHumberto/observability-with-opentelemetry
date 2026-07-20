package observability.order_service.controller;

import io.micrometer.core.instrument.MeterRegistry;
import observability.order_service.controller.dto.OrderRequest;
import observability.order_service.controller.dto.OrderResponse;
import observability.order_service.controller.dto.PaymentResponse;
import observability.order_service.service.PaymentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PaymentClient paymentClient;
    private final MeterRegistry meterRegistry;
    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(PaymentClient paymentClient, MeterRegistry meterRegistry) {
        this.paymentClient = paymentClient;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        log.info("Criando pedido: {}", request);
        // Métrica de contador de pedidos
        meterRegistry.counter("orders.created.total").increment();

        // Chamada síncrona ao payment-service (HTTP via RestClient ou WebClient)
        PaymentResponse payment = paymentClient.authorize(request);
        log.info("Pagamento autorizado: {}", payment);
        return ResponseEntity.ok(new OrderResponse(UUID.randomUUID().toString(), "APPROVED"));
    }
}
