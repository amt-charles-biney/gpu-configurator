package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
}
