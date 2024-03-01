package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationResponse;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.exception.InvalidPasswordException;
import com.amalitech.gpuconfigurator.model.User;

import java.security.Principal;

public interface ProfileService {
    BasicInformationResponse updateBasicInformation(BasicInformationRequest dto, Principal principal);

    BasicInformationResponse getUserProfile(Principal principal);

    GenericResponse updateUserPassword(UserPasswordRequest dto, Principal principal) throws InvalidPasswordException;

    ShippingResponse getUserShippingInformation(User user);

    ShippingResponse addUserShippingInformation(ShippingRequest dto, User user);
}
