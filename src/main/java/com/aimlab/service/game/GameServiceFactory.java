package com.aimlab.service.game;

import com.aimlab.common.exception.CustomException;
import com.aimlab.common.exception.ErrorCode;
import com.aimlab.domain.game.GameType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GameServiceFactory {
    private final Map<GameType, GameService> gameClients;

    /**
     * 게임 서비스 의존성 주입
     */
    public GameServiceFactory(Set<GameService> services){
        this.gameClients = services.stream()
                .collect(Collectors.toMap(GameService::supportGame, Function.identity()));
    }

    /**
     * 게임 서비스 반환
     * @param type 게임 타입
     * @return 게임 서비스(스프링 빈)
     */
    public GameService getService(GameType type){
        return Optional.ofNullable(gameClients.get(type))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_SUPPORTED_GAME_TYPE));
    }
}
