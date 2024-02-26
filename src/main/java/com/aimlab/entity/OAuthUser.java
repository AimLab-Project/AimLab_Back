package com.aimlab.entity;

import com.aimlab.common.security.oauth.OAuthServerType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oauth_user",
        uniqueConstraints = {
            @UniqueConstraint(name = "oauth_user_pk", columnNames = "oauth_user_id")
        }
)
public class OAuthUser {
    @Id
    @Column(name = "oauth_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oauthUserId;

    @Column(name = "oauth_id")
    private String oauthId;

    @Enumerated(EnumType.STRING)
    @Column(name = "server_type")
    private OAuthServerType oAuthServerType;

    @Column(name = "email")
    private String email;
}
