package com.amalitech.gpuconfigurator.service.cases;

import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CaseService {
    CaseResponse createCase(CreateCaseRequest dto, MultipartFile coverImage, List<MultipartFile> images);
}
