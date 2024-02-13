package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.service.cases.CaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CaseController {
    private final CaseService caseService;

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
}
