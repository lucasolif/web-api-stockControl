package br.com.autoflex.dto;

import java.math.BigDecimal;

public record MaterialItemResponse(
        Long rawMaterialId,
        Long rawMaterialCode,
        String rawMaterialName,
        BigDecimal requiredQuantity
) {}