package com.aimlab.service;

import com.aimlab.common.ErrorCode;
import com.aimlab.dto.TokenDto;
import com.aimlab.dto.UserDto;
import com.aimlab.entity.Authority;
import com.aimlab.entity.RefreshTokenEntity;
import com.aimlab.entity.User;
import com.aimlab.exception.CustomException;
import com.aimlab.security.JwtTokenProvider;
import com.aimlab.repository.RefreshTokenRepository;
import com.aimlab.repository.UserRepository;
import com.aimlab.security.UserPrincipal;
import com.aimlab.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 로직
     * @param userDto : 클라이언트가 전달한 유저정보
     * @return UserDto
     */
    @Transactional
    public UserDto signup(UserDto userDto){
        if(userRepository.findOneByUserEmail(userDto.getUserEmail()).orElse(null) != null){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");    // TODO : 추후 Exception 종류 변경
        }

        User user = User.builder()
                .userEmail(userDto.getUserEmail())
                .userPassword(passwordEncoder.encode(userDto.getUserPassword()))
                .userNickname(userDto.getUserNickname())
                .authority(Authority.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now()).build();

        return UserDto.fromEntity(userRepository.save(user));
    }

    /**
     * 일반 로그인 로직
     * @param email : 이메일
     * @param password : 비밀번호
     * @return JWT Token(Access Token, Refresh Token)
     */
    @Transactional
    public TokenDto login(String email,String password){
        // 1. 입력 받은 이메일, 비밀번호로 authenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 생성한 auth 정보의 유효성을 검사하고 유저 정보를 반환
        Authentication authentication = authenticationProvider.authenticate(authenticationToken);

        // 3. 유저 정보를 쓰레드 로컬에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. 사용자에게 반환할 JWT 토큰을 생성(AT, RT)
        TokenDto tokenDto = jwtTokenProvider.createTokens(authentication);

        // 5. 생성한 Refresh Token을 DB에 저장
        UUID userId = UUID.fromString(SecurityUtil.getCurrentUserId().orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        ));
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUserId(userId)
                .orElse(new RefreshTokenEntity());

        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setRefreshToken(tokenDto.getRefreshToken());
        refreshTokenEntity.setIssueAt(LocalDateTime.now());

        refreshTokenRepository.save(refreshTokenEntity);

        return tokenDto;
    }

    /**
     * refreshToken의 유효성을 검사해 토큰을 재발급하는 함수
     * @param refreshToken 리프레시 토큰
     * @return TokenDTO(AccessToken, RefreshToken)
     */
    @Transactional
    public TokenDto refreshTokens(String refreshToken){
        CustomException exception = new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        // 1. refreshToken 유효성 검사
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw exception;
        }

        // 2. UserId 추출
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String userId = ((UserPrincipal)authentication.getPrincipal()).getUserId();

        // 3. DB에 저장된 RT와 비교 (다르면 삭제)
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() -> exception);
        if(!refreshToken.equals(refreshTokenEntity.getRefreshToken())){
            refreshTokenRepository.deleteByUserId(UUID.fromString(userId));
            throw exception;
        }

        // 4. AT, RT 재발급
        return jwtTokenProvider.createTokens(authentication);
    }
}
