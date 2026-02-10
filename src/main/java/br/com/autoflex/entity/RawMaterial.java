package br.com.autoflex.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "raw_material")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private Long code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "stock_quantity", nullable = false, scale = 2)
    private BigDecimal stockQuantity;


    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RawMaterial that = (RawMaterial) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
