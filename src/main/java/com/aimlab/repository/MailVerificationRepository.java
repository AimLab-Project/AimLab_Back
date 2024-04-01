package com.aimlab.repository;

import com.aimlab.domain.MailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MailVerificationRepository extends JpaRepository<MailVerificationEntity, String> {
    Optional<MailVerificationEntity> findByKey(UUID key);
}
