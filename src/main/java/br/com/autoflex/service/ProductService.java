package br.com.autoflex.service;

import br.com.autoflex.dto.ProductResponse;
import br.com.autoflex.entity.Product;
import br.com.autoflex.mapper.ProductMapper;
import br.com.autoflex.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.productToResponse(product);
    }

}
