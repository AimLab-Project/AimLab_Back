package com.aimlab.domain.game;

import com.aimlab.domain.BaseTime;
import com.aimlab.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class GameBase extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User player;

    @Column(name = "play_time")
    private Long playTime;  // 게임 진행 시간(초단위)

    @Column(name = "is_complete")
    private boolean isComplete; // 게임 완료 여부

    @Column(name = "aim_enhancement_mode")
    private boolean isEnhanceMentMode;  // 에임 향상 모드 여부
}
