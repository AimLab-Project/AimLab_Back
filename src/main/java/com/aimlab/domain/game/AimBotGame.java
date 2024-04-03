package com.aimlab.domain.game;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class AimBotGame extends GameBase{
    @Column(name = "total_click_count")
    private Long totalClicks; // 총 클릭 수

    @Column(name = "hit_click_count")
    private Long hitClicks; // 명중된 클릭 수

    @Column(name = "miss_click_count")
    private Long missedClicks;  // 빗나간 클릭 수

    @Column(name = "headshot_count")
    private Long headshot;  // 헤드 샷 수

    @Column(name = "bodyshot_count")
    private Long bodyshot;  // 바디 샷 수

    @Column(name = "target_count")
    private Long targetCount; // 나타난 총 타겟 수

    @Embedded
    private HitRate hitRate;    // 명중률

    @Column(name = "avg_weak_distance")
    private Double avgWeakDistance; // 평균 취약 거리

    @Column(name = "avg_reaction_time")
    private Double avgReactionTime; // 평균 반응 속도
}
