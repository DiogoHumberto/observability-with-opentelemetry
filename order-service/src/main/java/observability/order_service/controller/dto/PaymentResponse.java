package observability.order_service.controller.dto;

import java.math.BigDecimal;

public record PaymentResponse(String authorizationCode, BigDecimal amount) {}
