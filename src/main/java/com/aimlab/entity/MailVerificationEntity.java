package com.aimlab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_verification")
public class MailVerificationEntity {
    @Id
    @Column(name = "verification_key")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID key;

    @Column(name = "email")
    private String email;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "expired_at")
    private LocalDateTime expiresAt;

    @Column(name = "retention_at")
    private LocalDateTime retentionAt;

    @Column(name = "is_confirm")
    private boolean isConfirm;
}
