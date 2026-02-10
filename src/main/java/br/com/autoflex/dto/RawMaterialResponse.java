package br.com.autoflex.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

public record RawMaterialResponse(
        Long id,
        Long code,
        String name,
        BigDecimal stockQuantity
) {}