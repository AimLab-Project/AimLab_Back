package com.aimlab.service;

import com.aimlab.common.exception.ErrorCode;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.dto.auth.SignUpDto;
import com.aimlab.dto.auth.TokenDto;
import com.aimlab.domain.user.Authority;
import com.aimlab.domain.user.LoginLog;
import com.aimlab.domain.RefreshTokenEntity;
import com.aimlab.domain.user.User;
import com.aimlab.common.exception.CustomException;
import com.aimlab.common.security.JwtTokenProvider;
import com.aimlab.repository.LoginLogRepository;
import com.aimlab.repository.RefreshTokenRepository;
import com.aimlab.repository.UserRepository;
import com.aimlab.common.security.UserPrincipal;
import com.aimlab.util.RequestUtil;
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
    private final LoginLogRepository loginLogRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    private final MailVerificationService mailVerificationService;

    /**
     * 회원가입 로직
     * @param request : 클라이언트가 전달한 유저정보
     */
    @Transactional
    public TokenDto signup(SignUpDto.Request request){
        String email = request.getUserEmail();
        String password = request.getUserPassword();
        String nickname = request.getUserNickname();
        String authKey = request.getKey();

        // 1. 인증 여부 확인
        mailVerificationService.checkConfirmedVerification(authKey, email);

        // 2. 이메일 사용여부 확인(일반 회원가입 여부만 확인, 비밀번호 유무로 판단)
        Optional<User> optionalUser = userRepository.findOneByUserEmail(email);
        if(optionalUser.isPresent() && optionalUser.get().getUserPassword() != null){
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        // 3. 회원 생성 & 저장
        User user = optionalUser.orElseGet(() -> User.builder()
                    .userEmail(email)
                    .authority(Authority.ROLE_USER).build());

        user.setUserPassword(passwordEncoder.encode(password));
        user.setUserNickname(nickname);
        userRepository.save(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(UserPrincipal.create(user), null, null);

        return jwtTokenProvider.createTokens(authentication);
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

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userId(userId)
                .refreshToken(tokenDto.getRefreshToken())
                .issueAt(LocalDateTime.now()).build();

        refreshTokenRepository.save(refreshTokenEntity);
        loginLogRepository.save(getNewLoginLog(userId));    // 로그인 로그 저장

        return tokenDto;
    }

    /**
     * refreshToken의 유효성을 검사해 토큰을 재발급하는 함수
     * @param refreshToken 리프레시 토큰
     * @return TokenDTO(AccessToken, RefreshToken)
     */
    @Transactional
    public TokenDto refreshTokens(String refreshToken){

        // 1. refreshToken 유효성 검사
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. UserId 추출
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String userId = ((UserPrincipal)authentication.getPrincipal()).getUserId();

        // 3. AT, RT 재발급
        TokenDto newTokens = jwtTokenProvider.createTokens(authentication);

        // 4. DB에 저장된 RT와 비교 (다르면 삭제)
        Optional<RefreshTokenEntity> optional = refreshTokenRepository.findByUserId(UUID.fromString(userId));
        if(optional.isEmpty() || !optional.get().getRefreshToken().equals(refreshToken)){
            refreshTokenRepository.deleteById(UUID.fromString(userId));
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 5. 새로 발급한 RT 저장
        RefreshTokenEntity refreshTokenEntity = optional.get();
        refreshTokenEntity.setNewToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        return newTokens;
    }

    /**
     * 성공 상태의 새로운 로그인 로그 엔티티 반환 (로그인 타입 : Local)
     */
    private LoginLog getNewLoginLog(UUID userId){
        return LoginLog.builder()
                .user(userRepository.getReferenceById(userId))
                .loginIp(RequestUtil.getRequestIp())
                .userAgent(RequestUtil.getUserAgent())
                .loginType(OAuthServerType.LOCAL)
                .isSuccess(true).build();
    }
}
