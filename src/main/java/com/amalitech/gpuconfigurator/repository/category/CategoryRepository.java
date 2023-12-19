package com.amalitech.gpuconfigurator.repository.category;

import com.amalitech.gpuconfigurator.model.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends CrudRepository<Category, UUID> {

    @Query("SELECT s FROM Category s WHERE s.categoryName = ?1 ")
    Optional<Category> findByCategoryName(String categoryName);
}
