package observability.order_service.service;

import observability.order_service.controller.dto.OrderRequest;
import observability.order_service.controller.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentClient {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(PaymentClient.class);

    public PaymentClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PaymentResponse authorize(OrderRequest request) {
        log.info("Chamando serviço de pagamento para pedido {}", request.orderId());
        return restClient.post()
                .uri("/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);
    }
}