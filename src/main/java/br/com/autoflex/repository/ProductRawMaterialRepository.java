package br.com.autoflex.repository;

import br.com.autoflex.entity.ProductRawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, Long> {

    @Query("""
        Select prm
        From ProductRawMaterial prm
        Join Fetch prm.product p
        Join Fetch prm.rawMaterial rm
    """)
    List<ProductRawMaterial> findAllWithProductAndRawMaterial();

    @Modifying
    @Query("DELETE FROM ProductRawMaterial prm WHERE prm.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}