package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.dto.cases.UserCaseResponse;
import com.amalitech.gpuconfigurator.service.cases.CaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CaseController {
    private final CaseService caseService;

    @Operation(
            summary = "Create a new case (for admins)",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/admin/cases")
    public ResponseEntity<CaseResponse> createCase(
            @Valid CreateCaseRequest dto,
            @RequestParam MultipartFile coverImage,
            @RequestParam List<MultipartFile> images
    ) {
        var response = caseService.createCase(dto, coverImage, images);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all cases (for admins)",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/cases")
    public ResponseEntity<Page<CaseResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "") String q
    ) {
        return ResponseEntity.ok(caseService.findAll(page, size, q));
    }

    @Operation(
            summary = "Get a case by ID (for admins)",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/cases/{caseId}")
    public ResponseEntity<CaseResponse> findById(@PathVariable UUID caseId) {
        return ResponseEntity.ok(caseService.findById(caseId));
    }

    @Operation(
            summary = "Get all cases for a user",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/cases")
    public ResponseEntity<Page<UserCaseResponse>> findAllForUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(caseService.findAllForUser(page, size));
    }

    @Operation(
            summary = "Get a case for a user by ID",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/cases/{caseId}")
    public ResponseEntity<UserCaseResponse> findForUserById(@PathVariable UUID caseId) {
        return ResponseEntity.ok(caseService.findForUserById(caseId));
    }

    @Operation(
            summary = "Update a case by ID (for admins)",
            method = "PUT"
    )
    @CrossOrigin
    @PutMapping("/v1/admin/cases/{caseId}")
    public ResponseEntity<CaseResponse> updateCase(
            @PathVariable UUID caseId,
            @Valid CreateCaseRequest dto,
            @RequestParam(required = false) MultipartFile coverImage,
            @RequestParam(required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.ok(caseService.updateCase(caseId, dto, coverImage, images));
    }

    @Operation(
            summary = "Delete a case by ID (for admins)",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/admin/cases/{caseId}")
    public ResponseEntity<GenericResponse> deleteById(@PathVariable UUID caseId) {
        return ResponseEntity.ok(caseService.deleteById(caseId));
    }
}
