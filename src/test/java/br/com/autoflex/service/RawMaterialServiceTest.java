package br.com.autoflex.service;

import br.com.autoflex.dto.RawMaterialRequest;
import br.com.autoflex.dto.RawMaterialResponse;
import br.com.autoflex.dto.RawMaterialUpdateRequest;
import br.com.autoflex.entity.RawMaterial;
import br.com.autoflex.exception.ConflictException;
import br.com.autoflex.exception.ResourceNotFoundException;
import br.com.autoflex.mapper.RawMaterialMapper;
import br.com.autoflex.repository.RawMaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock private RawMaterialMapper rawMaterialMapper;
    @Mock private RawMaterialRepository rawMaterialRepository;
    @InjectMocks private RawMaterialService rawMaterialService;

    @Test
    void listAll_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);

        RawMaterial rm1 = rawMaterial(1L, 10L, "Aço", bigDecimal("100.00"));
        RawMaterial rm2 = rawMaterial(2L, 20L, "Plástico", bigDecimal("50.50"));

        when(rawMaterialRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(rm1, rm2), pageable, 2));
        when(rawMaterialMapper.rawMaterialToResponse(rm1)).thenReturn(new RawMaterialResponse(1L, 10L, "Aço", bigDecimal("100.00")));
        when(rawMaterialMapper.rawMaterialToResponse(rm2)).thenReturn(new RawMaterialResponse(2L, 20L, "Plástico", bigDecimal("50.50")));

        Page<RawMaterialResponse> result = rawMaterialService.listAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Aço", result.getContent().get(0).name());

        verify(rawMaterialRepository).findAll(pageable);
        verify(rawMaterialMapper).rawMaterialToResponse(rm1);
        verify(rawMaterialMapper).rawMaterialToResponse(rm2);
    }

    @Test
    void getById_shouldReturnMappedResponse_whenExists() {
        Long id = 1L;
        RawMaterial entity = rawMaterial(id, 10L, "Aço", bigDecimal("100.00"));

        when(rawMaterialRepository.findById(id)).thenReturn(Optional.of(entity));
        when(rawMaterialMapper.rawMaterialToResponse(entity)).thenReturn(new RawMaterialResponse(id, 10L, "Aço", bigDecimal("100.00")));

        RawMaterialResponse res = rawMaterialService.getById(id);

        assertEquals(id, res.id());
        assertEquals("Aço", res.name());

        verify(rawMaterialRepository).findById(id);
        verify(rawMaterialMapper).rawMaterialToResponse(entity);
    }

    @Test
    void getById_shouldThrowResourceNotFound_whenNotExists() {
        when(rawMaterialRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rawMaterialService.getById(999L));

        verify(rawMaterialRepository).findById(999L);
        verifyNoInteractions(rawMaterialMapper);
    }

    @Test
    void saveRawMaterial_shouldSaveAndReturnResponse_whenValid() {
        RawMaterialRequest req = new RawMaterialRequest(10L, "Aço", bigDecimal("100.00"));

        RawMaterial mapped = rawMaterial(null, 10L, "Aço", bigDecimal("100.00"));
        RawMaterial saved = rawMaterial(1L, 10L, "Aço", bigDecimal("100.00"));

        when(rawMaterialMapper.rawMaterialRequestToEntity(req)).thenReturn(mapped);
        when(rawMaterialRepository.save(mapped)).thenReturn(saved);
        when(rawMaterialMapper.rawMaterialToResponse(saved)).thenReturn(new RawMaterialResponse(1L, 10L, "Aço", bigDecimal("100.00")));

        RawMaterialResponse res = rawMaterialService.saveRawMaterial(req);

        assertEquals(1L, res.id());
        assertEquals(10L, res.code());

        verify(rawMaterialMapper).rawMaterialRequestToEntity(req);
        verify(rawMaterialRepository).save(mapped);
        verify(rawMaterialMapper).rawMaterialToResponse(saved);
    }

    @Test
    void updateRawMaterial_shouldUpdateAndReturnResponse_whenExists() {
        Long id = 1L;

        RawMaterial existing = rawMaterial(id, 10L, "Antigo", bigDecimal("1.00"));
        RawMaterialUpdateRequest req = new RawMaterialUpdateRequest(20L, "Novo", bigDecimal("9.99"));

        RawMaterial saved = rawMaterial(id, 20L, "Novo", bigDecimal("9.99"));

        when(rawMaterialRepository.findById(id)).thenReturn(Optional.of(existing));
        when(rawMaterialRepository.save(existing)).thenReturn(saved);
        when(rawMaterialMapper.rawMaterialToResponse(saved)).thenReturn(new RawMaterialResponse(id, 20L, "Novo", bigDecimal("9.99")));

        RawMaterialResponse res = rawMaterialService.updateRawMaterial(id, req);

        assertEquals("Novo", res.name());
        assertEquals(20L, res.code());

        verify(rawMaterialRepository).findById(id);
        verify(rawMaterialRepository).save(existing);
        verify(rawMaterialMapper).rawMaterialToResponse(saved);
    }

    @Test
    void deleteRawMaterial_shouldThrowConflictException_whenInUse() {
        Long id = 1L;
        RawMaterial existing = rawMaterial(id, 10L, "Aço", bigDecimal("100.00"));

        when(rawMaterialRepository.findById(id)).thenReturn(Optional.of(existing));
        doThrow(new DataIntegrityViolationException("FK constraint")).when(rawMaterialRepository).delete(existing);

        ConflictException ex = assertThrows(ConflictException.class, () -> rawMaterialService.deleteRawMaterial(id));
        assertTrue(ex.getMessage().contains("Não é possível excluir"));

        verify(rawMaterialRepository).findById(id);
        verify(rawMaterialRepository).delete(existing);
        verify(rawMaterialRepository, never()).flush();
    }

    private static RawMaterial rawMaterial(Long id, Long code, String name, BigDecimal stock) {
        RawMaterial rm = new RawMaterial();
        rm.setId(id);
        rm.setCode(code);
        rm.setName(name);
        rm.setStockQuantity(stock);
        return rm;
    }

    private static BigDecimal bigDecimal(String v) {
        return new BigDecimal(v);
    }
}
