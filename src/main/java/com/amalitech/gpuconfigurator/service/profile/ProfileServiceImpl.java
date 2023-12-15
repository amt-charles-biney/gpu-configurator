package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.dto.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.UserPasswordRequest;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.contact.ContactService;
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

    @Override
    public void updateBasicInformation(BasicInformationRequest dto, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setContact(contactService.saveOrUpdate(user, dto.getContact()));
        userRepository.save(user);
    }

    @Override
    public boolean updateUserPassword(UserPasswordRequest dto, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if (passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
