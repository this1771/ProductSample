package com.example.productsample.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_cd")
    private Long productCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_cd", nullable = false)
    private BrandEntity brandEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_cd", nullable = false)
    private CategoryEntity categoryEntity;

    @Column(name = "name", nullable = false, length = 100)
    private String productName;

    @Column(name = "price", nullable = false, columnDefinition = "UNSIGNED INT")
    private Integer price;

    @Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean deleted;
}
