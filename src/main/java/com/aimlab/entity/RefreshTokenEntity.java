package com.aimlab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 사용자에게 발급한 refresh Token 테이블 <br>
 * 추후 redis로 이전 예정
 */
@Entity
@Table(name = "user_refresh_token")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity extends BaseTime{
    @Id
    private UUID userId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "issue_at")
    private LocalDateTime issueAt;
}
