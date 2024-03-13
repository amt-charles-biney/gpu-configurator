package com.amalitech.gpuconfigurator.model.PaymentInfo;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TypeAlias("MOBILE_MONEY")
public class MobilePayment extends PaymentInfoType {
    private String network;
    private String phoneNumber;
    private String iso2Code;
    private String dialCode;
    private String country;
}
