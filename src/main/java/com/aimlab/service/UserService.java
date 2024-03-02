package com.aimlab.service;

import com.aimlab.entity.User;
import com.aimlab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 일반 회원가입된 이메일 존재 여부
     * @param email 이메일
     * @return boolean 이메일 존재 여부
     */
    public boolean isEmailExist(String email){
        Optional<User> optional = userRepository.findOneByUserEmail(email);
        return optional.isPresent() && optional.get().getUserPassword() != null;
    }

    /**
     * 회원가입된 닉네임 존재 여부
     * @param nickname 닉네임
     * @return boolean 닉네임 존재 여부
     */
    public boolean isNicknameExist(String nickname){
        return userRepository.existsByUserNickname(nickname);
    }
}
