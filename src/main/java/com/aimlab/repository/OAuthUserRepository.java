package com.aimlab.repository;

import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.domain.user.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {
    Optional<OAuthUser> findByOauthIdAndOauthServerType(String oauthId, OAuthServerType oauthServerType);
}
