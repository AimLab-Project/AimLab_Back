package com.aimlab.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestEntity extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    long testId;

    @Column(name = "test_value")
    String testValue;
}
