package br.com.autoflex.service;

import br.com.autoflex.dto.ProductRequest;
import br.com.autoflex.dto.ProductResponse;
import br.com.autoflex.dto.ProductUpdateRequest;
import br.com.autoflex.entity.Product;
import br.com.autoflex.exception.ConflictException;
import br.com.autoflex.exception.ResourceNotFoundException;
import br.com.autoflex.mapper.ProductMapper;
import br.com.autoflex.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Page<ProductResponse> listAll(Pageable pageable){
        Page<Product> pageProduct = productRepository.findAll(pageable);
        return pageProduct.map(productMapper::productToResponse);
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper.productToResponse(product);
    }

    public ProductResponse saveProduct(ProductRequest productRequest){

        if (productRequest == null) {
            throw new IllegalArgumentException("Product request cannot be null");
        }

        Product product = productMapper.productRequestToEntity(productRequest);
        product = productRepository.save(product);

        return productMapper.productToResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest productRequest){
        Product existingProd = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (productRepository.existsByCodeAndIdNot(productRequest.code(), id)) {
            throw new ConflictException("Product code already exists");
        }

        existingProd.setName(productRequest.name());
        existingProd.setCode(productRequest.code());
        existingProd.setUnitPrice(productRequest.price());

        Product productSaved = productRepository.save(existingProd);
        return productMapper.productToResponse(productSaved);
    }

}
