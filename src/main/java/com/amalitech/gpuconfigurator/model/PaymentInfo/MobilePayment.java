package com.amalitech.gpuconfigurator.model.PaymentInfo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("MOBILE_MONEY")
public class MobilePayment extends PaymentInfoType {
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MobilePayment that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getPhoneNumber(), that.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPhoneNumber());
    }
}
