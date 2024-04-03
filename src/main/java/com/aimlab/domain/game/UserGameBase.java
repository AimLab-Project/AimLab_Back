package com.aimlab.domain.game;

import com.aimlab.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class UserGameBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User player;

    @Column(name = "highest_score")
    private Long highestScore;  // 최고 점수

    @Column(name = "total_score")
    private Long totalScore;    // 총 점수

    @Column(name = "play_count")
    private Long playCount;     // 플레이 판수

    @Column(name = "game_rank")
    private Long rank;          // 총 등수
}
