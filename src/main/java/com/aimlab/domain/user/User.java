package com.aimlab.domain.user;

import com.aimlab.domain.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * 기본 회원 정보 엔티티
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user",
        uniqueConstraints = {
            @UniqueConstraint(name = "email_unique", columnNames = "user_email"),
            @UniqueConstraint(name = "nickname_unique", columnNames = "user_nickname")
        }
)
public class User extends BaseTime {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(unique = true, name = "user_email")
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    /**
     * Setters
     */
    public void setUserPassword(String encodedPassword) {
        this.userPassword = encodedPassword;
    }

    public void setUserNickname(String nickname) {
        this.userNickname = nickname;
    }
}
