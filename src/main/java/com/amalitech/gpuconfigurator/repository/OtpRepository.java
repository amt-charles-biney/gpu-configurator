package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Otp;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepository extends CrudRepository<Otp, UUID> {
    Optional<Otp> findByCodeAndEmail(String code, String email);

    void deleteByCodeAndEmail(String code, String email);
}