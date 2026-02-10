package br.com.autoflex.service;


import br.com.autoflex.dto.RawMaterialRequest;
import br.com.autoflex.dto.RawMaterialResponse;
import br.com.autoflex.dto.RawMaterialUpdateRequest;
import br.com.autoflex.entity.RawMaterial;
import br.com.autoflex.exception.ResourceNotFoundException;
import br.com.autoflex.mapper.RawMaterialMapper;
import br.com.autoflex.repository.RawMaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RawMaterialService {

    private final RawMaterialMapper rawMaterialMapper;
    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterialService(RawMaterialMapper rawMaterialMapper, RawMaterialRepository rawMaterialRepository){
        this.rawMaterialRepository = rawMaterialRepository;
        this.rawMaterialMapper = rawMaterialMapper;
    }


    public Page<RawMaterialResponse> listAll(Pageable pageable){
        try{
            Page<RawMaterial> pageRawMaterial = rawMaterialRepository.findAll(pageable);
            return pageRawMaterial.map(rawMaterialMapper::rawMaterialToResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public RawMaterialResponse getById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Raw Material not found"));
        return rawMaterialMapper.rawMaterialToResponse(rawMaterial);
    }

    @Transactional(readOnly = true)
    public RawMaterial getEntityById(Long id) {
        return rawMaterialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Raw Material not found"));
    }

    @Transactional
    public RawMaterialResponse saveRawMaterial(RawMaterialRequest rawMaterialRequest){

        if (rawMaterialRequest == null) {
            throw new IllegalArgumentException("Raw Material request cannot be null");
        }

        try{
            RawMaterial rawMaterial = rawMaterialMapper.rawMaterialRequestToEntity(rawMaterialRequest);
            rawMaterial = rawMaterialRepository.save(rawMaterial);

            return rawMaterialMapper.rawMaterialToResponse(rawMaterial);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public RawMaterialResponse updateRawMaterial(Long id, RawMaterialUpdateRequest rawMatRequest){
        RawMaterial existingRawMat = rawMaterialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Raw Material not found"));

        existingRawMat.setName(rawMatRequest.name());
        existingRawMat.setCode(rawMatRequest.code());
        existingRawMat.setStockQuantity(rawMatRequest.stockQuantity());

        RawMaterial rawMaterialSaved = rawMaterialRepository.save(existingRawMat);
        return rawMaterialMapper.rawMaterialToResponse(rawMaterialSaved);
    }

    @Transactional
    public void deleteRawMaterial(Long id){
        RawMaterial existingRawMat = rawMaterialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Raw Material not found"));
        rawMaterialRepository.delete(existingRawMat);
    }

}
