package br.com.autoflex.controller;

import br.com.autoflex.dto.RawMaterialRequest;
import br.com.autoflex.dto.RawMaterialResponse;
import br.com.autoflex.dto.RawMaterialUpdateRequest;
import br.com.autoflex.service.RawMaterialService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/raw-material")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService){
        this.rawMaterialService = rawMaterialService;
    }


    @GetMapping
    public ResponseEntity<Page<RawMaterialResponse>> listProduct(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<RawMaterialResponse> page = rawMaterialService.listAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> getById(@PathVariable Long id) {
        RawMaterialResponse rawMat = rawMaterialService.getById(id);
        return ResponseEntity.ok(rawMat);
    }

    @PostMapping
    public ResponseEntity<RawMaterialResponse> create(@Valid @RequestBody RawMaterialRequest rawMaterialRequest){
        RawMaterialResponse rawMetFound = rawMaterialService.saveRawMaterial(rawMaterialRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(rawMetFound);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> update(@PathVariable Long id, @Valid @RequestBody RawMaterialUpdateRequest rawMaterialRequest){
        RawMaterialResponse updatedRawMaterial = rawMaterialService.updateRawMaterial(id, rawMaterialRequest);
        return ResponseEntity.ok(updatedRawMaterial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RawMaterialResponse> delete(@PathVariable Long id){
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
