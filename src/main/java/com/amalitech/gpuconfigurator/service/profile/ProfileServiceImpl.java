package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.constant.ProfileConstants;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationResponse;
import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.exception.InvalidPasswordException;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Contact;
import com.amalitech.gpuconfigurator.model.Shipping;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.ShippingRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.contact.ContactService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContactService contactService;
    private final ShippingRepository shippingRepository;

    @Override
    public BasicInformationResponse updateBasicInformation(BasicInformationRequest dto, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        if (dto.getContact() != null) {
            user.setContact(contactService.createOrUpdate(user, dto.getContact()));
        }

        userRepository.save(user);

        return userRepository.findBasicInformationByEmail(user.getEmail());
    }

    public BasicInformationResponse getUserProfile(Principal principal) {
        String email = principal.getName();

        return userRepository.findBasicInformationByEmail(email);
    }

    @Override
    public GenericResponse updateUserPassword(UserPasswordRequest dto, Principal principal) throws InvalidPasswordException {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new InvalidPasswordException(ProfileConstants.PASSWORDS_NOT_EQUAL);
        }
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException(ProfileConstants.INCORRECT_CURRENT_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return new GenericResponse(201, ProfileConstants.PASSWORD_UPDATE_SUCCESS);
    }

    @Override
    public ShippingResponse getUserShippingInformation(User user) {
        if (user.getShippingInformation() == null) {
            throw new NotFoundException("User has no shipping information");
        }
        return shippingRepository.findShippingResponseById(user.getShippingInformation().getId());
    }

    @Transactional
    @Override
    public ShippingResponse addUserShippingInformation(ShippingRequest dto, User user) {
        Shipping newShipping = mapShippingRequestToShipping(dto);

        Shipping savedShipping = shippingRepository.save(newShipping);

        user.setShippingInformation(savedShipping);
        userRepository.save(user);

        return shippingRepository.findShippingResponseById(savedShipping.getId());
    }

    private Shipping mapShippingRequestToShipping(ShippingRequest shippingRequest) {
        Shipping.ShippingBuilder builder = Shipping.builder()
                .firstName(shippingRequest.getFirstName())
                .lastName(shippingRequest.getLastName())
                .address1(shippingRequest.getAddress1())
                .country(shippingRequest.getCountry())
                .state(shippingRequest.getState())
                .city(shippingRequest.getCity())
                .zipCode(shippingRequest.getZipCode())
                .email(shippingRequest.getEmail())
                .contact(mapContactRequestToContact(shippingRequest.getContact()));

        if (shippingRequest.getAddress2() != null) {
            builder.address2(shippingRequest.getAddress2());
        }

        return builder.build();
    }

    private Contact mapContactRequestToContact(ContactRequest contactRequest) {
        return Contact.builder()
                .phoneNumber(contactRequest.getPhoneNumber())
                .country(contactRequest.getCountry())
                .iso2Code(contactRequest.getIso2Code())
                .dialCode(contactRequest.getDialCode())
                .build();
    }
}
