package com.aimlab.jwt;

import com.aimlab.config.AppProperties;
import com.aimlab.dto.TokenDto;
import com.aimlab.repository.RefreshTokenRepository;
import com.aimlab.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * 토큰 생성, 유효성 검증을 담당하는 TokenProvider
 */
@Component
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final AppProperties appProperties;
    private Key key;

    /**
     * Spring 설정 파일에 있는 jwt관련 설정 정보를 주입 받는다.
     */
    public JwtTokenProvider(AppProperties appProperties, RefreshTokenRepository refreshTokenRepository){
        this.appProperties = appProperties;
    }

    /**
     * Bean 초기화 후 secret을 base64 Decode한 후 key에 할당
     */
    @PostConstruct
    public void init() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getAuth().getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Authentication 객체의 권한 정보를 이용해 토큰 DTO를 리턴
     * accessToken, refreshToken 둘 다 생성
     */
    public TokenDto createTokens(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String userId = userPrincipal.getUserId();

        String accessToken = createToken(userId, appProperties.getAuth().getAccessTokenValidity());
        String refreshToken = createToken(userId, appProperties.getAuth().getRefreshTokenValidity());

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }

    public String createToken(String userId, long validity){
        long now = (new Date()).getTime();

        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(now + validity))
                .setIssuedAt(new Date(now))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 토큰안에 있는 권한정보를 이용해 Authentication 객체를 리턴
     */
    public Authentication getAuthentication(String token){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserPrincipal principal = new UserPrincipal(claims.getSubject(), null, null, null);

        return new UsernamePasswordAuthenticationToken(principal, token, null);
    }

    /**
     * JWT 토큰의 유효성을 검사하는 메서드
     * @param token JWT 토큰
     * @return boolean 유효성 검사 결과
     */
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명");
        } catch (ExpiredJwtException exception) {
            logger.info("만료된 JWT 토큰");
        } catch (UnsupportedJwtException exception){
            logger.info("지원되지 않는 JWT 토큰");
        } catch (IllegalArgumentException exception){
            logger.info("잘못된 JWT 토큰");
        }
        return false;
    }
}
