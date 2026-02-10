package br.com.autoflex.dto;

import java.math.BigDecimal;

public record ProductionSuggestionItemResponse(
        Long productId,
        Long productCode,
        String productName,
        BigDecimal unitPrice,
        Long quantityToProduce,
        BigDecimal totalValue
) {}
