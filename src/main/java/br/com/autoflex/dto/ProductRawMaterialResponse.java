package br.com.autoflex.dto;

import java.math.BigDecimal;

public record ProductRawMaterialResponse(
        Long rawMaterialId,
        Long rawMaterialCode,
        String rawMaterialName,
        BigDecimal quantityRequired
) {}
