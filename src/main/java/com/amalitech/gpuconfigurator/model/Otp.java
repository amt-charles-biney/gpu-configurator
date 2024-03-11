package com.amalitech.gpuconfigurator.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.amalitech.gpuconfigurator.model.enums.OtpType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private OtpType type;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Otp otp = (Otp) o;
        return Objects.equals(id, otp.id) && Objects.equals(code, otp.code) && Objects.equals(email, otp.email)
                && type == otp.type && Objects.equals(expiration, otp.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, email, type, expiration);
    }
}
