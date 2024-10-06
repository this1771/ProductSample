package com.example.productsample.repository;

import com.example.productsample.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findAllByOrderByCategoryCdAsc();
    Optional<CategoryEntity> findByCategoryName(String categoryName);
    Optional<CategoryEntity> findByCategoryCd(Integer categoryCd);
}
