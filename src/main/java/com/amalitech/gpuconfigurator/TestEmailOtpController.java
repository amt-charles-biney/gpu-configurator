package com.amalitech.gpuconfigurator;

import com.amalitech.gpuconfigurator.service.emailService.EmailService;
import com.amalitech.gpuconfigurator.service.emailService.EmailServiceImpl;
import com.amalitech.gpuconfigurator.service.otpService.OtpService;
import com.amalitech.gpuconfigurator.service.otpService.OtpServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestEmailOtpController {

    private final OtpService otpService;
    private final EmailService emailService;
    @PostMapping("/email")
    public ResponseEntity<String> testEmail(@RequestBody TestEmail test) throws MessagingException {
        String otp = otpService.generateOtp();
        emailService.sendOtpMessage(test.to(), otp);

        return ResponseEntity.ok("email sent successfully");

    }

}
