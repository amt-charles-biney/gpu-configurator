package com.amalitech.gpuconfigurator.model.PaymentInfo;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;

@Getter
@EqualsAndHashCode(callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TypeAlias("CARD")
public class CardPayment extends PaymentInfoType {
    private String cardNumber;
    private String expirationDate;
    private String cardholderName;
}
