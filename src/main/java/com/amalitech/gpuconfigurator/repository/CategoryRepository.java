package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Role.Category, UUID> {

    @Query("SELECT s FROM Category s WHERE s.categoryName = ?1 ")
   Optional<Role.Category> findByCategoryName(String categoryName);

    @Query("SELECT s FROM Category s WHERE s.categoryName = ?1 ")
    List<Role.Category> findByCategoryNameList(String categoryName);

}
