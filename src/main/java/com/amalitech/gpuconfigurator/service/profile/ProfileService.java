package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.dto.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.UserPasswordRequest;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.security.core.parameters.P;

import java.security.Principal;

public interface ProfileService {
    void updateBasicInformation(BasicInformationRequest dto, Principal principal);

    User getUserProfile(Principal principal);

    boolean updateUserPassword(UserPasswordRequest dto, Principal principal);
}
