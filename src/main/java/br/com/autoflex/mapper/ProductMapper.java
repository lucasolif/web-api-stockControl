package br.com.autoflex.mapper;

import lombok.Builder;
import lombok.Data;

import br.com.autoflex.dto.MaterialItemResponse;
import br.com.autoflex.dto.ProductResponse;
import br.com.autoflex.entity.Product;
import br.com.autoflex.entity.ProductRawMaterial;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class ProductMapper {

    public ProductResponse productToResponse(Product productEntity) {
        return new ProductResponse(
            productEntity.getId(),
            productEntity.getCode(),
            productEntity.getName(),
            productEntity.getUnitPrice(),
            mapProductRawMaterialToResponse(productEntity.getRawMaterials())
        );
    }

    private List<MaterialItemResponse> mapProductRawMaterialToResponse(Set<ProductRawMaterial> prodRawMat) {
        if (prodRawMat == null || prodRawMat.isEmpty()) return Collections.emptyList();

        return prodRawMat.stream()
            .filter(Objects::nonNull)
            .map(this::productRawMaterialToResponse)
            .collect(Collectors.toList());
    }

    private MaterialItemResponse productRawMaterialToResponse(ProductRawMaterial prm) {
        return new MaterialItemResponse(
            prm.getRawMaterial().getId(),
            prm.getRawMaterial().getCode(),
            prm.getRawMaterial().getName(),
            prm.getQuantityRequired()
        );
    }
}
