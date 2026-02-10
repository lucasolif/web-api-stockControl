package br.com.autoflex.service;

import br.com.autoflex.dto.ProductionSuggestionResponse;
import br.com.autoflex.dto.ProductionSuggestionItemResponse;
import br.com.autoflex.entity.Product;
import br.com.autoflex.entity.ProductRawMaterial;
import br.com.autoflex.entity.RawMaterial;
import br.com.autoflex.exception.BusinessException;
import br.com.autoflex.repository.ProductRawMaterialRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductionService {

    private final ProductRawMaterialRepository productRawMaterialRepository;

    public ProductionService (ProductRawMaterialRepository productRawMaterialRepository){
        this.productRawMaterialRepository = productRawMaterialRepository;
    }

    public ProductionSuggestionResponse suggestProduction(){

        try{
            List<ProductRawMaterial> meterials = productRawMaterialRepository.findAllWithProductAndRawMaterial();
            Map<Product, List<ProductRawMaterial>> groupProduct = meterials.stream().collect(Collectors.groupingBy(ProductRawMaterial::getProduct));
            Map<Long, BigDecimal> remainingStockByRawId = meterials.stream().map(ProductRawMaterial::getRawMaterial).distinct().collect(Collectors.toMap(RawMaterial::getId, rm -> rm.getStockQuantity() == null ? BigDecimal.ZERO : rm.getStockQuantity()));
            List<Product> orderedProducts = groupProduct.keySet().stream().sorted(Comparator.comparing(Product::getUnitPrice, Comparator.nullsLast(Comparator.reverseOrder()))).toList();

            List<ProductionSuggestionItemResponse> items = new ArrayList<>();
            BigDecimal grandTotal = BigDecimal.ZERO;

            for (Product product : orderedProducts) {

                List<ProductRawMaterial> prodRawMat = groupProduct.get(product);

                if (prodRawMat == null || prodRawMat.isEmpty()) continue;

                Long maxUnits = this.computeMaxUnits(prodRawMat, remainingStockByRawId);
                if (maxUnits <= 0) continue;

                for (ProductRawMaterial prm : prodRawMat) {
                    Long rawId = prm.getRawMaterial().getId();
                    BigDecimal needed = prm.getQuantityRequired().multiply(BigDecimal.valueOf(maxUnits));
                    remainingStockByRawId.put(rawId, remainingStockByRawId.get(rawId).subtract(needed));
                }

                BigDecimal totalValue = product.getUnitPrice().multiply(BigDecimal.valueOf(maxUnits));
                grandTotal = grandTotal.add(totalValue);

                items.add(new ProductionSuggestionItemResponse(
                        product.getId(),
                        product.getCode(),
                        product.getName(),
                        product.getUnitPrice(),
                        maxUnits,
                        totalValue
                ));
            }

            return new ProductionSuggestionResponse(items, grandTotal);
        }catch (BusinessException ex) {
            throw ex;

        } catch (Exception ex) {
            throw new BusinessException("Erro ao sugerir produção. Verifique dados de estoque ou cadastros.");
        }

    }

    private long computeMaxUnits(List<ProductRawMaterial> bom, Map<Long, BigDecimal> remainingStockByRawId) {
        Long min = null;

        for (ProductRawMaterial prm : bom) {
            BigDecimal required = prm.getQuantityRequired();
            if (required == null || required.signum() <= 0) return 0;
            BigDecimal stock = remainingStockByRawId.getOrDefault(prm.getRawMaterial().getId(), BigDecimal.ZERO);
            long possible = stock.divide(required, 0, RoundingMode.FLOOR).longValueExact();
            if (min == null || possible < min) min = possible;
        }
        return min == null ? 0 : min;
    }
}
