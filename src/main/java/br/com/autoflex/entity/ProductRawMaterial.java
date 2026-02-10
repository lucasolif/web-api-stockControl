package br.com.autoflex.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product_raw_material", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "raw_material_id"}))
public class ProductRawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(name = "quantity_required", nullable = false, precision = 18, scale = 2)
    private BigDecimal quantityRequired;


    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public BigDecimal getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(BigDecimal quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductRawMaterial that = (ProductRawMaterial) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
