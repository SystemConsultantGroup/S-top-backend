package com.scg.stop.domain.user.repository;

import com.scg.stop.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
