package observability.payment_service.controller;

import io.micrometer.core.instrument.MeterRegistry;
import observability.payment_service.controller.dto.OrderRequest;
import observability.payment_service.controller.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/authorize")
public class PaymentController {

    private final MeterRegistry meterRegistry;
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> authorize(@RequestBody OrderRequest request) {
        log.info("Autorizando pagamento do pedido {}", request.orderId());
        meterRegistry.counter("payments.authorized.total").increment();

        // Simula latência variável para enriquecer traces
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return ResponseEntity.ok(new PaymentResponse("AUTH-OK", request.amount()));
    }
}
