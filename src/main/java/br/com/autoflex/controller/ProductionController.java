package br.com.autoflex.controller;

import br.com.autoflex.dto.ProductionSuggestionResponse;
import br.com.autoflex.service.ProductionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<ProductionSuggestionResponse> suggestions() {
        ProductionSuggestionResponse suggestion = productionService.suggestProduction();
        return ResponseEntity.ok(suggestion);
    }
}