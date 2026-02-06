package br.com.autoflex.mapper;

import br.com.autoflex.dto.ProductRequest;

import br.com.autoflex.dto.RawMaterialRequest;
import br.com.autoflex.dto.RawMaterialResponse;
import br.com.autoflex.dto.ProductResponse;
import br.com.autoflex.entity.Product;
import br.com.autoflex.entity.ProductRawMaterial;
import br.com.autoflex.entity.RawMaterial;
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
            productEntity.getUnitPrice()
        );
    }

    public RawMaterialResponse productRawMaterialToResponse(RawMaterial rawMat) {
        return new RawMaterialResponse(
                rawMat.getId(),
                rawMat.getCode(),
                rawMat.getName(),
                rawMat.getStockQuantity()
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

    public RawMaterial productRawMaterialToEntity(RawMaterialRequest rawMatReq) {

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setId(rawMatReq.id());
        rawMaterial.setCode(rawMatReq.code());
        rawMaterial.setName(rawMatReq.name());
        rawMaterial.setStockQuantity(rawMatReq.stockQuantity());

        return  rawMaterial;
    }
}
