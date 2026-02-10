package br.com.autoflex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RawMaterialUpdateRequest(
        @NotNull Long code,
        @NotBlank String name,
        @NotNull BigDecimal stockQuantity
) {}
