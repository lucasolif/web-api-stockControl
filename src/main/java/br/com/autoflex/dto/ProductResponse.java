package br.com.autoflex.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        Long code,
        String name,
        BigDecimal price,
        List<ProductRawMaterialResponse> rawMaterials
) {}
