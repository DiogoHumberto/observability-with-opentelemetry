package observability.order_service.controller.dto;

import java.math.BigDecimal;

public record OrderRequest(String orderId, BigDecimal amount) {}
