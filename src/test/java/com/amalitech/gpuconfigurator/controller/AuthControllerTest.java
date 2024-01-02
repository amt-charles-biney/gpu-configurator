package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.auth.LoginDto;
import com.amalitech.gpuconfigurator.dto.auth.ResetPasswordDTO;
import com.amalitech.gpuconfigurator.dto.auth.SignUpDto;
import com.amalitech.gpuconfigurator.dto.otp.ResendOtpDto;
import com.amalitech.gpuconfigurator.dto.otp.VerifyOtpDTO;
import com.amalitech.gpuconfigurator.dto.otp.VerifyUserDto;
import com.amalitech.gpuconfigurator.dto.profile.ChangePasswordDTO;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.enums.OtpType;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.user.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthController authController;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSignupSuccess() throws Exception {
        SignUpDto signUpDto =  SignUpDto.builder()
                .firstName("dickson")
                .lastName("anyaele")
                .email("dickson@email.com")
                .password("dicksonanyaele1234")
                .build();

        String signUpDtoJson = objectMapper.writeValueAsString(signUpDto);

        doNothing().when(userService).signup(any(SignUpDto.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(signUpDtoJson));

        response.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testVerify() throws Exception {
        VerifyUserDto verifyUserDto = new VerifyUserDto("test@example.com", "12345");
        String verifyUserDtoJson = objectMapper.writeValueAsString(verifyUserDto);
        
        when(userService.verifyUserSignup(any(VerifyUserDto.class))).thenReturn(new AuthenticationResponse());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(verifyUserDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testResendOtp() throws Exception {
        ResendOtpDto resendOtpDto = new ResendOtpDto("test@example.com", String.valueOf(OtpType.CREATE));
        String resendOtpDtoJson = objectMapper.writeValueAsString(resendOtpDto);

        when(userService.resendOtp(any(ResendOtpDto.class))).thenReturn(new GenericResponse(201, "otp has been sent"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/resend-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resendOtpDtoJson)
                        .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        LoginDto loginDto = new LoginDto("test@example.com", "password");
        String loginDtoJson = objectMapper.writeValueAsString(loginDto);

        when(userService.login(any(LoginDto.class))).thenReturn(new AuthenticationResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDtoJson)
                        .characterEncoding("utf-8")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testResetPassword() throws Exception {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setEmail("dickson@email.com");
        String resetPasswordDTOJson = objectMapper.writeValueAsString(resetPasswordDTO);


        doNothing().when(userService).resetPassword(any(resetPasswordDTO.getClass()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resetPasswordDTOJson)
                        .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testChangePassword() throws Exception {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("john.doe@example.com", "123456", "newPassword123", "newPassword12");
        String changePasswordDTOJson = objectMapper.writeValueAsString(changePasswordDTO);

        when(userService.changePassword(any(changePasswordDTO.getClass()))).thenReturn(new GenericResponse(201, "user changed password successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordDTOJson)
                        .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
