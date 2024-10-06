package com.example.productsample.repository;

import com.example.productsample.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    boolean existsByBrandName(String brandName);
    Optional<BrandEntity> findByBrandCd(Integer brandCd);
    boolean existsByBrandNameAndBrandCdNot(String brandName, Integer brandCd);
}
