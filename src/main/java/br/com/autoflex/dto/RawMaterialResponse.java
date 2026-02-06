package br.com.autoflex.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

public record RawMaterialResponse(
        long id,
        long code,
        String name,
        BigDecimal stockQuantity
) {}