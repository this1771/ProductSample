package com.example.productsample.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

@Setter
@Getter
@Entity
@Accessors(chain = true)
@Table(name = "brand")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_cd")
    private Integer brandCd;

    @Column(name = "name", length = 40)
    private String brandName;

    @Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean deleted;
}
