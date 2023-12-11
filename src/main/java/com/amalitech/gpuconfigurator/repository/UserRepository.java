package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

}
