package com.amalitech.gpuconfigurator.service.cases;

import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Case;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CaseRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;
    private final AttributeOptionRepository attributeOptionRepository;
    private final UploadImageService imageUploadService;

    @Override
    public CaseResponse createCase(CreateCaseRequest dto, MultipartFile coverImage, List<MultipartFile> images) {
        List<AttributeOption> incompatibleVariants = new ArrayList<>();

        for (UUID variantId : dto.getIncompatibleVariants()) {
            var variant = attributeOptionRepository.findById(variantId)
                    .orElseThrow(() -> new NotFoundException("Variant with id " + variantId + " does not exist."));
            incompatibleVariants.add(variant);
        }

        String coverImageUrl = imageUploadService.uploadCoverImage(coverImage);
        List<String> imageUrls = images.stream().map(imageUploadService::upload).toList();

        var newCase = Case.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .coverImageUrl(coverImageUrl)
                .imageUrls(imageUrls)
                .incompatibleVariants(incompatibleVariants)
                .build();

        return mapCaseToCaseResponse(caseRepository.save(newCase));
    }


    private CaseResponse mapCaseToCaseResponse(Case productCase) {
        return CaseResponse.builder()
                .id(productCase.getId())
                .name(productCase.getName())
                .description(productCase.getDescription())
                .coverImageUrl(productCase.getCoverImageUrl())
                .imageUrls(productCase.getImageUrls())
                .price(productCase.getPrice())
                .incompatibleVariants(productCase.getIncompatibleVariants())
                .createdAt(productCase.getCreatedAt())
                .updatedAt(productCase.getUpdatedAt())
                .build();
    }
}