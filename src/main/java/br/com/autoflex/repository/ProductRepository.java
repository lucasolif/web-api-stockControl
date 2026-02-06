package br.com.autoflex.repository;

import br.com.autoflex.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(Long code, Long id);
}
