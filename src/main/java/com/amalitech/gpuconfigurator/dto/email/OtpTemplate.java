package com.amalitech.gpuconfigurator.dto.email;

import com.amalitech.gpuconfigurator.model.enums.OtpType;
import lombok.Builder;

@Builder
public record OtpTemplate(String to, String otp, OtpType otpType) {
}
