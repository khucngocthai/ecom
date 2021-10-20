package com.nashtech.ecommerce.repository;

import com.nashtech.ecommerce.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Optional<Category> findCategoryById(int id);

    public List<Category> findAll();

    public boolean existsByCategoryName(String name);
}
