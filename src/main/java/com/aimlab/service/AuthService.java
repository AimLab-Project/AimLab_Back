package com.aimlab.service;

import com.aimlab.common.exception.ErrorCode;
import com.aimlab.dto.authenticate.SignUpDto;
import com.aimlab.dto.authenticate.TokenDto;
import com.aimlab.entity.Authority;
import com.aimlab.entity.RefreshTokenEntity;
import com.aimlab.entity.User;
import com.aimlab.common.exception.CustomException;
import com.aimlab.common.security.JwtTokenProvider;
import com.aimlab.repository.RefreshTokenRepository;
import com.aimlab.repository.UserRepository;
import com.aimlab.common.security.UserPrincipal;
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
import java.util.Optional;
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
    private final MailVerificationService mailVerificationService;

    /**
     * 회원가입 로직
     * @param request : 클라이언트가 전달한 유저정보
     */
    @Transactional
    public void signup(SignUpDto.Request request){
        String email = request.getUserEmail();

        // 1. 이메일 사용여부 확인(일반 회원가입 여부만 확인, 비밀번호 유무로 판단)
        Optional<User> optionalUser = userRepository.findOneByUserEmail(email);
        if(optionalUser.isPresent() && optionalUser.get().getUserPassword() != null){
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        // 2. 인증 여부 확인
        mailVerificationService.checkConfirmedVerification(request.getKey(), email);

        // 3. 회원 생성 및 등록(새 회원 & 기존 소셜 회원)
        if(optionalUser.isEmpty()){
            User newUser = User.builder()
                    .userEmail(email)
                    .userPassword(passwordEncoder.encode(request.getUserPassword()))
                    .userNickname(request.getUserNickname())
                    .authority(Authority.ROLE_USER)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now()).build();
            userRepository.save(newUser);
        } else {
            User user = optionalUser.get();
            user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
            user.setUserNickname(request.getUserNickname());
            user.setModifiedAt(LocalDateTime.now());
            userRepository.save(user);
        }
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
        TokenDto newTokens = jwtTokenProvider.createTokens(authentication);

        // 5. 새로 발급한 RT 저장
        refreshTokenEntity.setRefreshToken(newTokens.getRefreshToken());
        refreshTokenEntity.setIssueAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshTokenEntity);

        return newTokens;
    }
}
