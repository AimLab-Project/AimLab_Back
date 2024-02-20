package com.aimlab.repository;

import com.aimlab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findOneByUserEmail(String userEmail);

    Optional<User> findByUserId(UUID userId);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserNickname(String userNickname);
}
