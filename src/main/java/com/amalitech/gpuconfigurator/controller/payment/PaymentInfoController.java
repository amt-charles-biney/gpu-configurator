package com.amalitech.gpuconfigurator.controller.payment;

import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyResponse;
import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;
import com.amalitech.gpuconfigurator.service.PaymentInfo.PaymentInfoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentInfoController {

    private final PaymentInfoServiceImpl paymentInfoService;

    @GetMapping("v1/payment_info/mobile_money")
    public ResponseEntity<ApiResponse<Optional<MobilePayment>>> getMobileMoneyInfo() {
        Optional<MobilePayment> paymentInfoTypes = paymentInfoService.getOneMobileMoneyPayment();
        return ResponseEntity.ok(new ApiResponse<>(paymentInfoTypes, "success", "200"));
    }

    @GetMapping("v1/payment_info/card")
    public ResponseEntity<ApiResponse<Optional<CardPayment>>> getCardInfo() {
        Optional<CardPayment> paymentInfoTypes = paymentInfoService.getOneCardPayment();
        return ResponseEntity.ok(new ApiResponse<>(paymentInfoTypes, "success", "200"));
    }

    @PostMapping("v1/payment_info/card")
    public ResponseEntity<ApiResponse<CardInfoResponse>> saveCardInfo(@RequestBody @Validated CardInfoRequest cardInfoRequest) throws InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        CardInfoResponse cardInfoResponse = paymentInfoService.saveCardPayment(cardInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(cardInfoResponse, "successfully created card", "201"));
    }

    @PostMapping("v1/payment_info/mobile_money")
    public ResponseEntity<ApiResponse<MobileMoneyResponse>> saveCardInfo(@RequestBody @Validated MobileMoneyRequest mobileMoneyRequest, Principal user) {
        MobileMoneyResponse mobileMoneyResponse = paymentInfoService.saveMobileMoneyPayment(mobileMoneyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(mobileMoneyResponse, "successfully added mobile money", "201"));
    }
}
