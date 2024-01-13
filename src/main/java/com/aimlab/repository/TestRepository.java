package com.aimlab.repository;

import com.aimlab.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
    Optional<TestEntity> findByTestId(long testId);
    boolean existsByTestId(long testId);
}
