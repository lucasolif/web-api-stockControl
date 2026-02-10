package br.com.autoflex.mapper;

import br.com.autoflex.dto.ProductRawMaterialResponse;
import br.com.autoflex.dto.ProductRequest;

import br.com.autoflex.dto.ProductResponse;
import br.com.autoflex.dto.RawMaterialResponse;
import br.com.autoflex.entity.Product;

import br.com.autoflex.entity.RawMaterial;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProductMapper {

    public ProductResponse productToResponse(Product productEntity) {

        if (productEntity == null) return null;

        List<ProductRawMaterialResponse> rawMaterials = productEntity.getRawMaterials() == null
                ? List.of()
                : productEntity.getRawMaterials().stream()
                .map(prm -> new ProductRawMaterialResponse(
                        prm.getRawMaterial().getId(),
                        prm.getRawMaterial().getCode(),
                        prm.getRawMaterial().getName(),
                        prm.getQuantityRequired()
                ))
                .toList();

        return new ProductResponse(
            productEntity.getId(),
            productEntity.getCode(),
            productEntity.getName(),
            productEntity.getUnitPrice(),
            rawMaterials
        );
    }

    public Product productRequestToEntity(ProductRequest productRequest){

        if (productRequest == null) return null;

        Product product = new Product();
        product.setCode(productRequest.code());
        product.setName(productRequest.name());
        product.setUnitPrice(productRequest.price());

        return product;
    }

    public List<ProductResponse> EntityListToProductResponse(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }

        return products.stream()
                .map(this::productToResponse)
                .toList();
    }
}
