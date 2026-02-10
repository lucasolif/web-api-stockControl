package br.com.autoflex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotNull Long code,
        @NotBlank String name,
        @NotNull BigDecimal price,
        @NotNull List<ProductRawMaterialRequest> rawMaterials
) {}
