package com.amalitech.gpuconfigurator.service.cases;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.dto.cases.UserCaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CaseService {
    CaseResponse createCase(CreateCaseRequest dto, MultipartFile coverImage, List<MultipartFile> images);

    CaseResponse updateCase(UUID caseId, CreateCaseRequest dto, MultipartFile coverImage, List<MultipartFile> images);

    Page<CaseResponse> findAll(int page, int size);

    CaseResponse findById(UUID caseId);

    GenericResponse deleteById(UUID caseId);

    Page<UserCaseResponse> findAllForUser(int page, int size);

    UserCaseResponse findForUserById(UUID caseId);
}
