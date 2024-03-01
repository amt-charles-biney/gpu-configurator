package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
}
