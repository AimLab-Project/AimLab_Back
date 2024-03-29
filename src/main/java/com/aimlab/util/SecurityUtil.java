package com.aimlab.util;

import com.aimlab.common.security.UserPrincipal;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@NoArgsConstructor
public class SecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    public static Optional<String> getCurrentUserId(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            logger.info("현재 인증정보가 없습니다");
            return Optional.empty();
        }

        String userId = null;
        if(authentication.getPrincipal() instanceof UserPrincipal user){
            userId = user.getUserId();
        } else if (authentication.getPrincipal() instanceof UserDetails user) {
            userId = user.getUsername();
        }else if(authentication.getPrincipal() instanceof String) {
            userId = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(userId);
    }

    public static Optional<UserPrincipal> getCurrentUser(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            logger.info("현재 인증정보가 없습니다");
            return Optional.empty();
        }

        if(authentication.getPrincipal() instanceof UserPrincipal user){
            return Optional.ofNullable(user);
        }

        return Optional.empty();
    }
}
