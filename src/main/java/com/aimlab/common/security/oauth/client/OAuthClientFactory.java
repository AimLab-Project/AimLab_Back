package com.aimlab.common.security.oauth.client;

import com.aimlab.common.exception.CustomException;
import com.aimlab.common.exception.ErrorCode;
import com.aimlab.common.security.oauth.OAuthServerType;
import com.aimlab.common.security.oauth.model.OAuthUserDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * OAuth Client Factory<br>
 * 모든 OAuth Client를 Map형태로 소유한 Factory 클래스
 */
@Component
public class OAuthClientFactory {
    private final Map<OAuthServerType, OAuthClient> clients;

    /**
     * 스프링 빈을 등록된 모든 OAuthClient를 Map형태로 초기화하는 생성자 주입
     * @param clients OAuth Client의 집합
     */
    public OAuthClientFactory(Set<OAuthClient> clients) {
        this.clients = clients.stream()
                .collect(Collectors.toMap(OAuthClient::supportServer, Function.identity()));
    }

    /**
     * 각 OAuth 서비스에 해당하는 로그인 페이지 uri
     * @param serverType OAuth 서비스 제공자 타입
     * @return Redirect Uri
     */
    public String getLoginRedirectUri(OAuthServerType serverType){
        return getClient(serverType).getLoginRedirectUri();
    }

    /**
     * 각 OAuth 서비스에 해당하는 fetch함수를 호출해주는 Factory 함수
     * @param serverType OAuth 서비스 제공자 타입
     * @param code Authentication Code
     * @return OAuthUserDto 소셜 유저 객체 인터페이스
     */
    public OAuthUserDto fetch(OAuthServerType serverType, String code){
        return getClient(serverType).fetch(code);
    }

    private OAuthClient getClient(OAuthServerType serverType){
        return Optional.ofNullable(clients.get(serverType))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_SUPPORTED_OAUTH_SERVER));
    }
}
