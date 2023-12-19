package com.amalitech.gpuconfigurator.repository.category;

import com.amalitech.gpuconfigurator.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

//    @Query("SELECT s FROM Category s WHERE s.categoryName = ?1 ")
   Category findByCategoryName(String categoryName);

}
