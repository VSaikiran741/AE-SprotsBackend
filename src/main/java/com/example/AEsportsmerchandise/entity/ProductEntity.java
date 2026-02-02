    package com.example.AEsportsmerchandise.entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    @Table(name = "products")
    @Getter
    @Setter
    public class ProductEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 200)
        private String slug;

        @Column(nullable = false, length = 150)
        private String name;

        @Column(nullable = false, length = 1000)
        private String description;

        @Column(length = 300)
        private String shortDescription;

        @Column(nullable = false, length = 100)
        private String category;

        @Column(nullable = false, length = 100)
        private String brand;
        @Column(nullable = false)
        private int attemptCount = 0;


        @Column(name = "country_code", nullable = false, length = 2)
        private String countryCode;


        @Column(precision = 10, scale = 2, nullable = false)
        private BigDecimal basePrice;

        @Column(precision = 10, scale = 2)
        private BigDecimal discountPrice;

        private Integer discountPercent;


        private double rating=0.0;

        @Column(nullable = false)
        private Integer reviewCount = 0;

        @Column(nullable = false)
        private Boolean active = true;

        private Boolean returnable;
        private Integer returnWindowDays;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ProductVariantEntity> variants;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ProductImageEntity> images;
        @OneToMany(mappedBy = "product")
        private List<OrderItemEntity> orderItems;


        @PrePersist
        public void prePersist() {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
            if (this.slug == null) {
                String baseSlug = name
                        .toLowerCase()
                        .replaceAll("[^a-z0-9]+", "-")
                        .replaceAll("(^-|-$)", "");
                this.slug = baseSlug + "-" + java.util.UUID.randomUUID().toString().substring(0, 6);
            }
        }

        @PreUpdate
        public void preUpdate() {
            this.updatedAt = LocalDateTime.now();
        }
    }