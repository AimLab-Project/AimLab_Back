package com.aimlab.service;

import com.aimlab.entity.TestEntity;
import com.aimlab.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository repository;

    @Transactional
    public TestEntity getTestData(){
        repository.save(TestEntity.builder()
                .testValue("hello world").build());

        Optional<TestEntity> byTestId = repository.findTopByOrderByTestIdDesc();
        return byTestId
                .orElseThrow();
    }
}
