package com.aimlab.service;

import com.aimlab.common.exception.ErrorCode;
import com.aimlab.entity.User;
import com.aimlab.common.exception.CustomException;
import com.aimlab.repository.UserRepository;
import com.aimlab.common.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String user_email) throws UsernameNotFoundException {
        return userRepository.findOneByUserEmail(user_email)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(user_email + "에 해당하는 회원을 찾을 수 없습니다."));
    }

    @Transactional
    public UserDetails loadUserByUserId(UUID userId){
        return userRepository.findByUserId(userId)
                .map(this::createUser)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
    }

    private UserDetails createUser(User user){
        return UserPrincipal.create(user);
    }
}
