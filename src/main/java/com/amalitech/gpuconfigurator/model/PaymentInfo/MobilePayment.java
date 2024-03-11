package com.amalitech.gpuconfigurator.model.PaymentInfo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TypeAlias("MOBILE_MONEY")
public class MobilePayment extends PaymentInfoType {
    private String phoneNumber;
}
