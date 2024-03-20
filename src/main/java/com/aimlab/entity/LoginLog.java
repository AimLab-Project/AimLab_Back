package com.aimlab.entity;

import com.aimlab.common.security.oauth.OAuthServerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Login Log 테이블
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "login_log")
public class LoginLog extends BaseTime{
    @Id
    @Column(name = "login_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "login_ip")
    private String loginIp;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "is_success")
    private boolean isSuccess;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private OAuthServerType loginType;


    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
