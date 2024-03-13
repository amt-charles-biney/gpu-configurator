package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.constant.ProfileConstants;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.profile.BasicInformationResponse;
import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import com.amalitech.gpuconfigurator.dto.profile.ContactResponse;
import com.amalitech.gpuconfigurator.exception.InvalidPasswordException;
import com.amalitech.gpuconfigurator.model.Contact;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.contact.ContactServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTests {
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private ContactServiceImpl contactService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsernamePasswordAuthenticationToken authenticationToken;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Contact contact;
    private User user;
    private BasicInformationRequest basicInformationRequestDto;
    private UserPasswordRequest userPasswordRequestDto;
    private BasicInformationResponse basicInformationResponse;
    private ContactResponse contactResponse;

    @BeforeEach
    public void init() {
        contact = new Contact();
        contact.setPhoneNumber("0245678901");
        contact.setCountry("Ghana");
        contact.setIso2Code("GH");
        contact.setDialCode("+233");

        user = new User();

        basicInformationRequestDto = BasicInformationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .contact(ContactRequest.builder()
                        .phoneNumber("0245678901")
                        .country("Ghana")
                        .iso2Code("GH")
                        .dialCode("+233")
                        .build())
                .build();

        userPasswordRequestDto = UserPasswordRequest.builder()
                .currentPassword("current-password")
                .newPassword("new-password")
                .confirmNewPassword("new-password")
                .build();

        contactResponse = projectionFactory.createProjection(ContactResponse.class);
        contactResponse.setCountry("Ghana");
        contactResponse.setDialCode("+233");
        contactResponse.setPhoneNumber("0245678901");
        contactResponse.setIso2Code("GH");

        basicInformationResponse = projectionFactory.createProjection(BasicInformationResponse.class);
        basicInformationResponse.setContact(contactResponse);
        basicInformationResponse.setEmail("john.doe@example.com");
        basicInformationResponse.setFirstName("John");
        basicInformationResponse.setLastName("Doe");
    }

    @Test
    public void updateBasicInformation_whenValidInput_updatesBasicInformation() {
        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(contactService.createOrUpdate(any(User.class), any(ContactRequest.class))).thenReturn(contact);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findBasicInformationByEmail(user.getEmail())).thenReturn(basicInformationResponse);

        BasicInformationResponse response = profileService.updateBasicInformation(basicInformationRequestDto, authenticationToken);

        verify(userRepository, times(1)).save(user);

        Assertions.assertThat(user.getFirstName()).isEqualTo("John");
        Assertions.assertThat(user.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(user.getContact()).isEqualTo(contact);
        Assertions.assertThat(user.getContact().getPhoneNumber()).isEqualTo(contact.getPhoneNumber());
        Assertions.assertThat(user.getContact().getCountry()).isEqualTo(contact.getCountry());
        Assertions.assertThat(user.getContact().getIso2Code()).isEqualTo(contact.getIso2Code());
        Assertions.assertThat(user.getContact().getDialCode()).isEqualTo(contact.getDialCode());
    }

    @Test
    public void getUserProfile_whenAuthenticatedUser_returnUserProfile() {
        user.setFirstName("John");
        user.setEmail("john.doe@example.com");
        when(authenticationToken.getName()).thenReturn(user.getEmail());
        when(userRepository.findBasicInformationByEmail(user.getEmail())).thenReturn(basicInformationResponse);

        BasicInformationResponse response = profileService.getUserProfile(authenticationToken);

        Assertions.assertThat(response.getFirstName()).isEqualTo("John");
        Assertions.assertThat(response.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        Assertions.assertThat(response.getContact()).isEqualTo(contactResponse);
    }

    @Test
    public void updateUserPassword_whenValidInput_updatesPasswordSuccessfully() {
        String encodedOldPassword = "encoded-old-password";
        String encodedNewPassword = "encoded-new-password";

        user.setPassword(encodedOldPassword);

        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenReturn(true);
        when(passwordEncoder.encode(any(String.class))).thenReturn(encodedNewPassword);

        GenericResponse response = profileService.updateUserPassword(userPasswordRequestDto, authenticationToken);

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).matches(userPasswordRequestDto.getCurrentPassword(), encodedOldPassword);
        verify(passwordEncoder, times(1)).encode(userPasswordRequestDto.getNewPassword());

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.message()).isEqualTo(ProfileConstants.PASSWORD_UPDATE_SUCCESS);
        Assertions.assertThat(user.getPassword()).isEqualTo(encodedNewPassword);
    }

    @Test
    public void updateUserPassword_whenIncorrectCurrentPassword_throwsException() {
        String encodedOldPassword = "encoded-old-password";

        userPasswordRequestDto.setCurrentPassword("incorrect-password");
        user.setPassword(encodedOldPassword);

        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenReturn(false);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> {
            profileService.updateUserPassword(userPasswordRequestDto, authenticationToken);
        });

        verify(passwordEncoder, times(1)).matches(userPasswordRequestDto.getCurrentPassword(), encodedOldPassword);
        verify(passwordEncoder, never()).encode(userPasswordRequestDto.getNewPassword());
        verify(userRepository, never()).save(user);

        Assertions.assertThat(exception.getMessage()).isEqualTo(ProfileConstants.INCORRECT_CURRENT_PASSWORD);
    }

    @Test
    public void updateUserPassword_whenNewPasswordAndConfirmNewPasswordDoNotMatch_throwsException() {
        String encodedOldPassword = "encoded-old-password";

        userPasswordRequestDto.setNewPassword("new-password");
        userPasswordRequestDto.setConfirmNewPassword("not-new-password");
        user.setPassword(encodedOldPassword);

        when(authenticationToken.getPrincipal()).thenReturn(user);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> {
            profileService.updateUserPassword(userPasswordRequestDto, authenticationToken);
        });

        verify(passwordEncoder, never()).matches(userPasswordRequestDto.getCurrentPassword(), encodedOldPassword);
        verify(passwordEncoder, never()).encode(userPasswordRequestDto.getNewPassword());
        verify(userRepository, never()).save(user);

        Assertions.assertThat(exception.getMessage()).isEqualTo(ProfileConstants.PASSWORDS_NOT_EQUAL);
    }
}
