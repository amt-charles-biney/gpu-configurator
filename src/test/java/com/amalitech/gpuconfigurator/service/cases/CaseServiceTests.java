package com.amalitech.gpuconfigurator.service.cases;

import com.amalitech.gpuconfigurator.dto.cases.CaseResponse;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Case;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CaseRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseServiceTests {
    @InjectMocks
    private CaseServiceImpl caseService;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private AttributeOptionRepository attributeOptionRepository;

    @Mock
    private UploadImageService imageUploadService;

    private Case productCase;

    private CreateCaseRequest createCaseRequestDto;

    private MultipartFile coverImage;

    private List<MultipartFile> images;

    private AttributeOption variant;

    private String coverImageUrl;

    private List<String> imageUrls;

    @BeforeEach
    public void init() {
        String caseName = "Test Case";
        String caseDescription = "Description for test case";
        BigDecimal casePrice = new BigDecimal("10.99");
        List<UUID> incompatibleVariantIds = new ArrayList<>(List.of(UUID.fromString("2d187a4b-4013-4337-87ec-90531a2fb8b8")));
        coverImageUrl = "https://media.url/freqwfads";
        imageUrls = List.of("https://media.url/rqewirorewf", "https://media.url/rqewirorewf", "https://media.url/rqewirorewf");

        createCaseRequestDto = CreateCaseRequest.builder()
                .name(caseName)
                .description(caseDescription)
                .price(casePrice)
                .incompatibleVariants(incompatibleVariantIds)
                .build();

        coverImage = new MockMultipartFile("coverImage", new byte[0]);

        images = new ArrayList<>();
        images.add(new MockMultipartFile("image1", new byte[0]));
        images.add(new MockMultipartFile("image2", new byte[0]));
        images.add(new MockMultipartFile("image3", new byte[0]));

        variant = AttributeOption.builder()
                .id(incompatibleVariantIds.getFirst())
                .build();

        productCase = Case.builder()
                .name(caseName)
                .description(caseDescription)
                .coverImageUrl(coverImageUrl)
                .imageUrls(imageUrls)
                .price(casePrice)
                .incompatibleVariants(List.of(variant))
                .build();
    }

    @Test
    public void createCase_whenValidInput_returnsCaseResponse() {
        when(attributeOptionRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(variant));
        when(imageUploadService.uploadCoverImage(any(MultipartFile.class))).thenReturn(coverImageUrl);
        when(imageUploadService.upload(any(MultipartFile.class))).thenReturn(imageUrls.getFirst());
        when(caseRepository.save(any(Case.class))).thenReturn(productCase);

        CaseResponse result = caseService.createCase(createCaseRequestDto, coverImage, images);

        verify(caseRepository, times(1)).save(any(Case.class));

        Assertions.assertThat(createCaseRequestDto.getName()).isEqualTo(result.getName());
        Assertions.assertThat(createCaseRequestDto.getDescription()).isEqualTo(result.getDescription());
        Assertions.assertThat(coverImageUrl).isEqualTo(result.getCoverImageUrl());
        Assertions.assertThat(imageUrls).isEqualTo(result.getImageUrls());
        Assertions.assertThat(createCaseRequestDto.getPrice()).isEqualTo(result.getPrice());
        Assertions.assertThat(variant.getId()).isEqualTo(result.getIncompatibleVariants().getFirst().getId());
    }

    @Test
    public void createCase_whenInvalidVariantId_throwsNotFoundException() {
        when(attributeOptionRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> caseService.createCase(createCaseRequestDto, coverImage, images));

        verify(imageUploadService, never()).uploadCoverImage(any(MultipartFile.class));
        verify(imageUploadService, never()).upload(any(MultipartFile.class));
        verify(caseRepository, never()).save(any(Case.class));

        Assertions.assertThat(exception.getMessage()).isNotEmpty();
    }

    @Test
    public void findById_whenValidCaseId_returnsCaseResponse() {
        UUID caseId = UUID.randomUUID();

        productCase.setId(caseId);

        when(caseRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(productCase));

        CaseResponse result = caseService.findById(caseId);

        verify(caseRepository, times(1)).findById(caseId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(caseId);
    }

    @Test
    public void findById_whenInvalidCaseId_throwsNotFoundException() {
        UUID invalidId = UUID.randomUUID();

        when(caseRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> caseService.findById(invalidId));

        verify(caseRepository, times(1)).findById(invalidId);

        Assertions.assertThat(exception.getMessage()).isNotEmpty();
    }
}
