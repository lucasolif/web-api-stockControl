package br.com.autoflex.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductionSuggestionResponse(
        List<ProductionSuggestionItemResponse> items,
        BigDecimal grandTotalValue
) {}
