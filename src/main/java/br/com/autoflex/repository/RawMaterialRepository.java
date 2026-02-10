package br.com.autoflex.repository;

import br.com.autoflex.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    boolean existsByCode(Long code);
    boolean existsByCodeAndIdNot(Long code, Long id);
}
