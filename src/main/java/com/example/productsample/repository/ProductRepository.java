package com.example.productsample.repository;

import com.example.productsample.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = """
        SELECT
            c.name AS categoryName
            , b.name AS brandName
            , p.product_cd AS productCd
            , p.name AS productName
            , p.price AS lowestPrice
        FROM (
            SELECT
                product_cd
                , p.name
                , p.brand_cd
                , category_cd
                , price
                , ROW_NUMBER() OVER (PARTITION BY category_cd ORDER BY price, p.brand_cd DESC) AS num
            FROM product p
            INNER JOIN brand b ON (b.brand_cd = p.brand_cd)
            WHERE b.deleted = 0
              AND p.deleted = 0
            
        )p
        INNER JOIN brand b ON (b.brand_cd = p.brand_cd)
        INNER JOIN category c ON (c.category_cd = p.category_cd)
        WHERE p.num = 1
        ORDER BY p.category_cd
    """, nativeQuery = true)
    Optional<List<Object[]>> lowestPrice();

    @Query(value = """
        SELECT
            c.name AS categoryName
            , b.name AS brandName
            , p.product_cd AS productCd
            , p.name AS productName
            , p.price AS lowestPrice
        FROM (
            SELECT
                product_cd
                , p.name
                , p.brand_cd
                , category_cd
                , price
                , ROW_NUMBER() OVER (PARTITION BY p.brand_cd, category_cd ORDER BY price, category_cd DESC) AS num
            FROM product p
            INNER JOIN brand b ON (b.brand_cd = p.brand_cd)
            WHERE b.deleted = 0
              AND p.deleted = 0
        )p
        INNER JOIN brand b ON (b.brand_cd = p.brand_cd)
        INNER JOIN category c ON (c.category_cd = p.category_cd)
        WHERE num  = 1
        ORDER BY p.brand_cd, p.category_cd
    """, nativeQuery = true)
    Optional<List<Object[]>> lowestPriceOfBrands();

    @Query(value = """
        SELECT
            c.name AS categoryName
            , b.name AS brandName
            , p.product_cd AS productCd
            , p.name AS productName
            , p.price
            , p.priceType
        FROM (
            (SELECT
                product_cd
                , p.name
                , p.brand_cd
                , category_cd
                , price
                , ROW_NUMBER() OVER (PARTITION BY category_cd ORDER BY price, p.brand_cd DESC) AS num
                , 'MIN' AS priceType
            FROM product p
            INNER JOIN brand b ON (b.brand_cd = p.brand_cd)
            WHERE TRUE
              AND b.deleted = 0
              AND p.category_cd = :categoryCd
              AND p.deleted = 0
            ORDER BY num
            LIMIT 1)
    
            UNION ALL
    
            (SELECT
                product_cd
                , p.name
                , p.brand_cd
                , category_cd
                , price
                , ROW_NUMBER() OVER (PARTITION BY category_cd ORDER BY price DESC, p.brand_cd DESC) AS num
                , 'MAX' AS priceType
            FROM product p
            INNER JOIN brand b ON (b.brand_cd = p.brand_cd)
            WHERE TRUE
              AND b.deleted = 0
              AND p.category_cd = :categoryCd
              AND p.deleted = 0
            ORDER BY num
            LIMIT 1)
        ) p
        INNER JOIN brand b ON (b.brand_cd = p.brand_cd)
        INNER JOIN category c ON (c.category_cd = p.category_cd)
    """, nativeQuery = true)
    Optional<List<Object[]>> priceRangeByCategory(@Param("categoryCd") Integer categoryCd);

    Optional<ProductEntity> findByProductCd(Long productCd);
}
