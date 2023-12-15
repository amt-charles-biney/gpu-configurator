package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.dto.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.UserPasswordRequest;

import java.security.Principal;

public interface ProfileService {
    void updateBasicInformation(BasicInformationRequest dto, Principal principal);

    boolean updateUserPassword(UserPasswordRequest dto, Principal principal);
}
