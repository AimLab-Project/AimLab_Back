package com.aimlab.service;

import com.aimlab.dto.UserDto;
import com.aimlab.entity.Authority;
import com.aimlab.entity.User;
import com.aimlab.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 로직
     * @param userDto : 클라이언트가 전달한 유저정보
     * @return UserDto
     */
    @Transactional
    public UserDto signup(UserDto userDto){
        if(userRepository.findOneByUserEmail(userDto.getUser_email()).orElse(null) != null){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        User user = User.builder()
                .userEmail(userDto.getUser_email())
                .userPassword(passwordEncoder.encode(userDto.getUser_password()))
                .userNickname(userDto.getUser_nickname())
                .authority(Authority.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now()).build();

        return UserDto.fromEntity(userRepository.save(user));
    }
}
