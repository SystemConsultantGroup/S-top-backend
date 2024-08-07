package com.scg.stop.user.repository;

import com.scg.stop.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findBySocialLoginId(String socialLoginId);
}