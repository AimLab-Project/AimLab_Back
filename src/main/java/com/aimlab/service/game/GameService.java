package com.aimlab.service.game;

import com.aimlab.domain.game.GameType;

public interface GameService {
    /**
     * 새로운 게임 생성
     */
    void createGame();

    /**
     * 게임 결과 저장
     */
    void saveGameResult(Long gameId);

    /**
     * 지원하는 게임 타입
     * @return GameType
     */
    GameType supportGame();
}
