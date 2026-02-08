package com.example.AEsportsmerchandise.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_variants",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "size", "color"})
        }
)
@Getter
@Setter
public class ProductVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false, length = 30)
    private String size;

    @Column(nullable = false, length = 30)
    private String color;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;
    private BigDecimal basePrice;
    private BigDecimal discountPrice;
    private Integer discountPercent;

    // 🔥 CRITICAL FOR PHASE 5
    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock = 0;

    @Column(nullable = false)
    private Boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.sku == null) {
            this.sku = buildSku();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private String buildSku() {
        int year = java.time.LocalDate.now().getYear();

        return String.join("-",
                product.getCategory().toUpperCase(),
                product.getBrand().toUpperCase(),
                color.toUpperCase(),
                size.toUpperCase(),
                String.valueOf(year)
        );
    }
}
