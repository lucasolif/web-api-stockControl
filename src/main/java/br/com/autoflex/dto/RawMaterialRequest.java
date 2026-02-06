package br.com.autoflex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RawMaterialRequest(
        @NotNull long id,
        @NotNull long code,
        @NotBlank String name,
        @NotNull BigDecimal stockQuantity
) {}
