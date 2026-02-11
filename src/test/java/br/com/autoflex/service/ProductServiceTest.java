package br.com.autoflex.service;

import br.com.autoflex.dto.ProductRawMaterialRequest;
import br.com.autoflex.dto.ProductRequest;
import br.com.autoflex.dto.ProductResponse;
import br.com.autoflex.dto.ProductUpdateRequest;
import br.com.autoflex.entity.Product;
import br.com.autoflex.entity.RawMaterial;
import br.com.autoflex.exception.ResourceNotFoundException;
import br.com.autoflex.mapper.ProductMapper;
import br.com.autoflex.repository.ProductRawMaterialRepository;
import br.com.autoflex.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductMapper productMapper;
    @Mock private RawMaterialService rawMaterialService;
    @Mock private ProductRawMaterialRepository productRawMaterialRepository;

    @InjectMocks private ProductService productService;

    @Test
    void listAll_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Product p1 = product(1L, 100L, "P1", bigDecimal("10.00"));
        Product p2 = product(2L, 200L, "P2", bigDecimal("20.00"));

        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(p1, p2), pageable, 2));
        when(productMapper.productToResponse(p1)).thenReturn(new ProductResponse(1L, 100L, "P1", bigDecimal("10.00"), List.of()));
        when(productMapper.productToResponse(p2)).thenReturn(new ProductResponse(2L, 200L, "P2", bigDecimal("20.00"), List.of()));

        Page<ProductResponse> result = productService.listAll(pageable);
        assertEquals(2, result.getTotalElements());
        verify(productRepository).findAll(pageable);
        verify(productMapper).productToResponse(p1);
        verify(productMapper).productToResponse(p2);
    }

    @Test
    void getById_shouldReturnMappedResponse_whenExists() {
        Long id = 1L;
        Product entity = product(id, 100L, "Produto", bigDecimal("12.34"));

        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(productMapper.productToResponse(entity)).thenReturn(new ProductResponse(id, 100L, "Produto", bigDecimal("12.34"), List.of()));

        ProductResponse res = productService.getById(id);
        assertEquals(id, res.id());
        assertEquals(100L, res.code());
        verify(productRepository).findById(id);
        verify(productMapper).productToResponse(entity);
    }

    @Test
    void getById_shouldThrowResourceNotFound_whenNotExists() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getById(999L));
        verify(productRepository).findById(999L);
        verifyNoInteractions(productMapper);
    }

    @Test
    void saveProduct_shouldSaveAndReturnResponse_whenValid() {
        ProductRequest req = new ProductRequest(123L, "Produto X", bigDecimal("9.99"),
                List.of(new ProductRawMaterialRequest(10L, bigDecimal("2.5"))));

        Product mappedEntity = product(null, 123L, "Produto X", bigDecimal("9.99"));

        RawMaterial rm = new RawMaterial();
        rm.setId(10L);

        Product saved = product(1L, 123L, "Produto X", bigDecimal("9.99"));

        when(productMapper.productRequestToEntity(req)).thenReturn(mappedEntity);
        when(rawMaterialService.getEntityById(10L)).thenReturn(rm);
        when(productRepository.save(mappedEntity)).thenReturn(saved);
        when(productMapper.productToResponse(saved)).thenReturn(new ProductResponse(1L, 123L, "Produto X", bigDecimal("9.99"), List.of()));

        ProductResponse res = productService.saveProduct(req);
        assertEquals(1L, res.id());
        assertEquals("Produto X", res.name());

        verify(rawMaterialService).getEntityById(10L);
        verify(productRepository).save(mappedEntity);
        verify(productMapper).productToResponse(saved);
    }

    @Test
    void saveProduct_shouldThrowRuntimeException_whenNoRawMaterials() {
        ProductRequest req = new ProductRequest(123L, "Produto X", bigDecimal("9.99"), List.of());
        Product mappedEntity = product(null, 123L, "Produto X", bigDecimal("9.99"));
        when(productMapper.productRequestToEntity(req)).thenReturn(mappedEntity);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.saveProduct(req));
        assertTrue(ex.getMessage().contains("Erro ao salvar produto"));

        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_shouldUpdateDeleteBomAndSave_whenValid() {
        Long id = 1L;

        Product existing = product(id, 111L, "Antigo", bigDecimal("1.00"));
        existing.setRawMaterials(new java.util.ArrayList<>());

        ProductUpdateRequest req = new ProductUpdateRequest(222L, "Novo", bigDecimal("2.50"), List.of(new ProductRawMaterialRequest(10L, bigDecimal("3.0"))));

        RawMaterial rm = new RawMaterial();
        rm.setId(10L);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(rawMaterialService.getEntityById(10L)).thenReturn(rm);
        when(productRepository.save(existing)).thenReturn(existing);
        when(productMapper.productToResponse(existing)).thenReturn(new ProductResponse(id, 222L, "Novo", bigDecimal("2.50"), List.of()));

        ProductResponse res = productService.updateProduct(id, req);
        assertEquals("Novo", res.name());
        assertEquals(222L, res.code());

        verify(productRawMaterialRepository).deleteByProductId(id);
        verify(productRawMaterialRepository).flush();
        verify(productRepository).save(existing);
    }

    private static Product product(Long id, Long code, String name, BigDecimal price) {
        Product p = new Product();
        p.setId(id);
        p.setCode(code);
        p.setName(name);
        p.setUnitPrice(price);
        return p;
    }

    private static BigDecimal bigDecimal(String v) {
        return new BigDecimal(v);
    }
}
