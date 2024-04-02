package com.amalitech.gpuconfigurator.controller.payment;

import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.CardInfoResponse;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyRequest;
import com.amalitech.gpuconfigurator.dto.PaymentInfo.MobileMoneyResponse;
import com.amalitech.gpuconfigurator.model.PaymentInfo.CardPayment;
import com.amalitech.gpuconfigurator.model.PaymentInfo.MobilePayment;
import com.amalitech.gpuconfigurator.service.PaymentInfo.PaymentInfoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Payment-Info")
@RequiredArgsConstructor
public class PaymentInfoController {

    private final PaymentInfoServiceImpl paymentInfoService;

    @Operation(
            description = "Mobile money Payment",
            summary = "This is for getting mobile money payment as a channel for all the channels",
            method = "GET"
    )
    @GetMapping("v1/payment_info/mobile_money")
    public ResponseEntity<ApiResponse<List<MobileMoneyResponse>>> getMobileMoneyInfo() {
        List<MobileMoneyResponse> paymentInfoTypes = paymentInfoService.getAllMobilePaymentByUser();
        return ResponseEntity.ok(new ApiResponse<>(paymentInfoTypes, "success", "200"));
    }

    @Operation(
            description = "Mobile money Payment",
            summary = "This is for getting mobile money payment as a channel for a single channel",
            method = "GET"
    )
    @GetMapping("v1/payment_info/mobile_money/{paymentInfoId}")
    public ResponseEntity<ApiResponse<MobileMoneyResponse>> getOneMobileMoneyInfo(@PathVariable String paymentInfoId) {
        MobileMoneyResponse paymentInfoType = paymentInfoService.getOneMobileMoneyPaymentInfo(paymentInfoId);
        return ResponseEntity.ok(new ApiResponse<>(paymentInfoType, "success", "200"));
    }

    @Operation(
            description = "Credit Card Payment",
            summary = "This is for getting credit card payment as a channel for all channel",
            method = "GET"
    )
    @GetMapping("v1/payment_info/card")
    public ResponseEntity<ApiResponse<List<CardInfoResponse>>> getCardInfo() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        List<CardInfoResponse> paymentInfoTypes = paymentInfoService.getAllCardPaymentByUser();
        return ResponseEntity.ok(new ApiResponse<>(paymentInfoTypes, "success", "200"));
    }

    @Operation(
            description = "Credit Card Payment",
            summary = "This is for getting credit card payment as a channel for a single channel",
            method = "GET"
    )
    @GetMapping("v1/payment_info/card/{paymentInfoId}")
    public ResponseEntity<ApiResponse<CardInfoResponse>> getOneCardInfo(@PathVariable String paymentInfoId) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        CardInfoResponse paymentInfoType = paymentInfoService.getOneCardPaymentInfo(paymentInfoId);
        return ResponseEntity.ok(new ApiResponse<>(paymentInfoType, "success", "200"));
    }

    @Operation(
            description = "Credit Card Payment",
            summary = "This is for saving card information",
            method = "POST"
    )
    @PostMapping("v1/payment_info/card")
    public ResponseEntity<ApiResponse<CardInfoResponse>> saveCardInfo(@RequestBody @Validated CardInfoRequest cardInfoRequest) throws InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        CardInfoResponse cardInfoResponse = paymentInfoService.saveCardPayment(cardInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(cardInfoResponse, "successfully created card", "201"));
    }

    @Operation(
            description = "Save payment info",
            summary = "This is for saving mobile money information",
            method = "POST"
    )
    @PostMapping("v1/payment_info/mobile_money")
    public ResponseEntity<ApiResponse<MobileMoneyResponse>> saveCardInfo(@RequestBody @Validated MobileMoneyRequest mobileMoneyRequest) {
        MobileMoneyResponse mobileMoneyResponse = paymentInfoService.saveMobileMoneyPayment(mobileMoneyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(mobileMoneyResponse, "successfully added mobile money", "201"));
    }

    @Operation(
            description = "Delete Payment info",
            summary = "This is for deleting a payment info",
            method = "DELETE"
    )
    @DeleteMapping("v1/payment_info/{paymentInfoId}")
    public ResponseEntity<ApiResponse> deletePaymentInfo(@PathVariable String paymentInfoId) {
        GenericResponse result = paymentInfoService.deletePaymentInfo(paymentInfoId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>(null, result.message(), String.valueOf(result.status())));
    }
}
