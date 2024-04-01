package com.aimlab.repository;

import com.aimlab.domain.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
    public Optional<TestEntity> findTopByOrderByTestIdDesc();
}
