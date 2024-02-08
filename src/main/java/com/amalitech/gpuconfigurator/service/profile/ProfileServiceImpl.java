package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.constant.ProfileConstants;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationResponse;
import com.amalitech.gpuconfigurator.dto.profile.ContactResponse;
import com.amalitech.gpuconfigurator.exception.InvalidPasswordException;
import com.amalitech.gpuconfigurator.model.Contact;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.contact.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContactService contactService;

    @Override
    public GenericResponse updateBasicInformation(BasicInformationRequest dto, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setContact(contactService.createOrUpdate(user, dto.getContact()));

        userRepository.save(user);

        return new GenericResponse(200, ProfileConstants.BASIC_INFORMATION_UPDATE_SUCCESS);
    }

    public BasicInformationResponse getUserProfile(Principal principal) throws UsernameNotFoundException {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return BasicInformationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .contact(this.mapToContactResponse(user.getContact()))
                .build();
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

    private ContactResponse mapToContactResponse(Contact contact) {
        if (contact == null) {
            return null;
        }
        return ContactResponse.builder()
                .country(contact.getCountry())
                .dialCode(contact.getDialCode())
                .iso2Code(contact.getIso2Code())
                .phoneNumber(contact.getPhoneNumber())
                .build();
    }
}
