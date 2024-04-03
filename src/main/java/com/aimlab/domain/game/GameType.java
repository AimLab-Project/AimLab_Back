package com.aimlab.domain.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {
    PRACTICE_MODE(1),  // 연습 모드
    SHOOTING_MODE(2),  // 사격 모드
    AIMBOT_MODE(3);    // 에임 봇 모드

    private final int code;
}
