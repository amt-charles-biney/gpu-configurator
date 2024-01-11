package com.amalitech.gpuconfigurator.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImageServiceImpl {
     String upload(MultipartFile file);
    String uploadCoverImage(MultipartFile coverImage);

}