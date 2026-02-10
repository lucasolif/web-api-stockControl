package br.com.autoflex.dto;

import java.math.BigDecimal;

public record ProductRawMaterialRequest(
        Long rawMaterialId,
        BigDecimal quantityRequired
) {}