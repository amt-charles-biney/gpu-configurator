package com.amalitech.gpuconfigurator.service.cases;

import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.model.Case;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CaseService {
    CaseResponse createCase(CreateCaseRequest dto, MultipartFile coverImage, List<MultipartFile> images);

    CaseResponse updateCase(UUID caseId, CreateCaseRequest dto, MultipartFile coverImage, List<MultipartFile> images);
}
