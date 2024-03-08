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
@DiscriminatorValue("CARD")
public class CardPayment extends PaymentInfoType {
    private String cardNumber;
    private String expirationDate;
    private String cardholderName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardPayment that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getCardNumber(), that.getCardNumber()) && Objects.equals(getExpirationDate(), that.getExpirationDate()) && Objects.equals(getCardholderName(), that.getCardholderName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCardNumber(), getExpirationDate(), getCardholderName());
    }
}
