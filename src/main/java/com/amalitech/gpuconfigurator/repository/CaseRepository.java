package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.dto.cases.UserCaseResponse;
import com.amalitech.gpuconfigurator.model.Case;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CaseRepository extends JpaRepository<Case, UUID> {
    Optional<Case> findByName(String name);
    
    Page<UserCaseResponse> findAllForUserBy(Pageable pageable);

    Optional<UserCaseResponse> findForUserById(UUID caseId);
    Page<Case> findAllByNameContainingIgnoreCase(String query, PageRequest pageRequest);
}
