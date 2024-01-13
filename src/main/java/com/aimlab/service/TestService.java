package com.aimlab.service;

import com.aimlab.entity.TestEntity;
import com.aimlab.repository.TestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TestService {
    private final TestRepository repository;

    @Transactional
    public String getTestData(){
        if(!repository.existsByTestId(1)){
            repository.save(new TestEntity(1, "hello world"));
        }

        Optional<TestEntity> byTestId = repository.findByTestId(1);
        return byTestId
                .orElseThrow()
                .getTestValue();
    }
}
