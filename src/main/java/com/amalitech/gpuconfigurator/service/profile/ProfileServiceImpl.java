package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.constant.ProfileConstants;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.dto.profile.*;
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
    public ProfileShippingResponse getUserShippingInformation(User user) {
        if (user.getShippingInformation() == null) {
            throw new NotFoundException("User has no shipping information");
        }

        Shipping shipping = shippingRepository.findById(user.getShippingInformation().getId())
                .orElseThrow(() -> new NotFoundException("User has no shipping information"));

        ProfileShippingResponse profileShippingResponse = mapToProfileShippingResponse(shipping);

        if (shipping.getEmail() == null) {
            profileShippingResponse.setEmail(user.getEmail());
        }

        return profileShippingResponse;
    }

    @Transactional
    @Override
    public ShippingResponse addUserShippingInformation(ProfileShippingRequest dto, User user) {
        Shipping newShipping = mapProfileShippingRequestToShipping(dto);

        Shipping savedShipping = shippingRepository.save(newShipping);

        user.setShippingInformation(savedShipping);
        userRepository.save(user);

        return shippingRepository.findShippingResponseById(savedShipping.getId());
    }

    private Shipping mapProfileShippingRequestToShipping(ProfileShippingRequest shippingRequest) {
        Shipping.ShippingBuilder builder = Shipping.builder()
                .firstName(shippingRequest.getFirstName())
                .lastName(shippingRequest.getLastName())
                .address1(shippingRequest.getAddress1())
                .country(shippingRequest.getCountry())
                .state(shippingRequest.getState())
                .city(shippingRequest.getCity())
                .zipCode(shippingRequest.getZipCode())
                .email(shippingRequest.getEmail())
                .contact(Contact.builder()
                        .country(shippingRequest.getContact().getCountry())
                        .phoneNumber(shippingRequest.getContact().getPhoneNumber())
                        .iso2Code(shippingRequest.getContact().getIso2Code())
                        .dialCode(shippingRequest.getContact().getDialCode())
                        .build());

        if (shippingRequest.getAddress2() != null) {
            builder.address2(shippingRequest.getAddress2());
        }

        return builder.build();
    }

    private ProfileShippingResponse mapToProfileShippingResponse(Shipping shipping) {
        return ProfileShippingResponse.builder()
                .id(shipping.getId())
                .firstName(shipping.getFirstName())
                .lastName(shipping.getLastName())
                .address1(shipping.getAddress1())
                .address2(shipping.getAddress2())
                .country(shipping.getCountry())
                .state(shipping.getState())
                .city(shipping.getCity())
                .zipCode(shipping.getZipCode())
                .email(shipping.getEmail())
                .contact(mapToProfileContactResponse(shipping.getContact()))
                .build();
    }

    private ProfileContactResponse mapToProfileContactResponse(Contact contact) {
        return ProfileContactResponse.builder()
                .phoneNumber(contact.getPhoneNumber())
                .dialCode(contact.getDialCode())
                .iso2Code(contact.getIso2Code())
                .country(contact.getCountry())
                .build();
    }
}
