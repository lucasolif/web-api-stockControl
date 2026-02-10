package br.com.autoflex.mapper;


import br.com.autoflex.dto.RawMaterialRequest;
import br.com.autoflex.dto.RawMaterialResponse;
import br.com.autoflex.entity.RawMaterial;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RawMaterialMapper {

    public RawMaterialResponse rawMaterialToResponse(RawMaterial rawMat) {
        return new RawMaterialResponse(
                rawMat.getId(),
                rawMat.getCode(),
                rawMat.getName(),
                rawMat.getStockQuantity()
        );
    }

    public RawMaterial rawMaterialRequestToEntity(RawMaterialRequest rawMatReq) {

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode(rawMatReq.code());
        rawMaterial.setName(rawMatReq.name());
        rawMaterial.setStockQuantity(rawMatReq.stockQuantity());

        return  rawMaterial;
    }

    public List<RawMaterialResponse> EntityListToRawMaterialResponse(List<RawMaterial> rawMatEntity) {
        if (rawMatEntity == null || rawMatEntity.isEmpty()) {
            return List.of();
        }

        return rawMatEntity.stream()
                .map(this::rawMaterialToResponse)
                .toList();
    }

}
