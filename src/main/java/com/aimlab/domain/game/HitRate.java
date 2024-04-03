package com.aimlab.domain.game;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class HitRate {
    @Column(name = "total_avg_hr")
    private Double avgHitRate;      // 전체 평균 명중률 (1 게임 기준)

    @Column(name = "one_avg_hr")
    private Double oneAvgHitRate;    // 1 사분면 평균 명중률

    @Column(name = "two_avg_hr")
    private Double twoAvgHitRate;   // 2 사분면 평균 명중률

    @Column(name = "three_avg_hr")
    private Double threeAvgHitRate;   // 3 사분면 평균 명중률

    @Column(name = "four_avg_hr")
    private Double fourAvgHitRate;   // 4 사분면 평균 명중률

    public HitRate(Double avgHitRate, Double oneAvgHitRate, Double twoAvgHitRate, Double threeAvgHitRate, Double fourAvgHitRate) {
        this.avgHitRate = avgHitRate;
        this.oneAvgHitRate = oneAvgHitRate;
        this.twoAvgHitRate = twoAvgHitRate;
        this.threeAvgHitRate = threeAvgHitRate;
        this.fourAvgHitRate = fourAvgHitRate;
    }

    public HitRate() {

    }
}
