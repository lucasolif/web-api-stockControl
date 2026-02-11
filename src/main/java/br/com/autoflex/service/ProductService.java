package br.com.autoflex.service;

import br.com.autoflex.dto.*;
import br.com.autoflex.entity.Product;
import br.com.autoflex.entity.ProductRawMaterial;
import br.com.autoflex.entity.RawMaterial;
import br.com.autoflex.exception.BusinessException;
import br.com.autoflex.exception.ResourceNotFoundException;
import br.com.autoflex.mapper.ProductMapper;
import br.com.autoflex.repository.ProductRawMaterialRepository;
import br.com.autoflex.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RawMaterialService rawMaterialService;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, RawMaterialService rawMaterialService, ProductRawMaterialRepository productRawMaterialRepository){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.rawMaterialService = rawMaterialService;
        this.productRawMaterialRepository = productRawMaterialRepository;
    }

    public Page<ProductResponse> listAll(Pageable pageable){
        try{
            Page<Product> pageProduct = productRepository.findAll(pageable);
            return pageProduct.map(productMapper::productToResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return productMapper.productToResponse(product);
    }

    @Transactional
    public ProductResponse saveProduct(ProductRequest productRequest){

        if (productRequest == null) throw new IllegalArgumentException("Product request cannot be null");

        try {
            Product product = productMapper.productRequestToEntity(productRequest);
            this.listProdRawMatForCreate(product, productRequest.rawMaterials());
            product = productRepository.save(product);

            return productMapper.productToResponse(product);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produto", e);
        }
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest productRequest){
        try{
            Product existingProd = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
            existingProd.setName(productRequest.name());
            existingProd.setCode(productRequest.code());
            existingProd.setUnitPrice(productRequest.price());

            this.listProdRawMatForUpdate(existingProd, productRequest.rawMaterials());

            Product productSaved = productRepository.save(existingProd);
            return productMapper.productToResponse(productSaved);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produto", e);
        }

    }

    @Transactional
    public void deleteProduct(Long id){
        Product existingProd = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        productRepository.delete(existingProd);
    }

    private void listProdRawMatForCreate(Product product, List<ProductRawMaterialRequest> itemsPrm) {

        if (product.getRawMaterials() == null) {
            product.setRawMaterials(new ArrayList<>());
        }

        product.getRawMaterials().clear();
        product.getRawMaterials().addAll(buildMaterialsList(product, itemsPrm));
    }

    private void listProdRawMatForUpdate(Product product, List<ProductRawMaterialRequest> itemsPrm) {
        productRawMaterialRepository.deleteByProductId(product.getId());
        productRawMaterialRepository.flush();
        product.getRawMaterials().clear();
        product.getRawMaterials().addAll(buildMaterialsList(product, itemsPrm));
    }

    private List<ProductRawMaterial> buildMaterialsList(Product product, List<ProductRawMaterialRequest> prodRawMat) {

        if (prodRawMat == null || prodRawMat.isEmpty()) {
            throw new BusinessException("O produto deve conter pelo menos uma matéria-prima.");
        }

        Set<Long> usedRawIds = new HashSet<>();
        List<ProductRawMaterial> newUsedRawIds = new ArrayList<>();

        for (ProductRawMaterialRequest item : prodRawMat) {

            if (item.rawMaterialId() == null || item.quantityRequired() == null || item.quantityRequired().signum() <= 0) {
                throw new BusinessException("Não foi informado a matéria prima");
            }
            if (!usedRawIds.add(item.rawMaterialId())) {
                throw new IllegalArgumentException("Matéria prima duplicadas: " + item.rawMaterialId());
            }

            RawMaterial rm = rawMaterialService.getEntityById(item.rawMaterialId());

            ProductRawMaterial prm = new ProductRawMaterial();
            prm.setProduct(product);
            prm.setRawMaterial(rm);
            prm.setQuantityRequired(item.quantityRequired());

            newUsedRawIds.add(prm);
        }

        return newUsedRawIds;
    }


}
