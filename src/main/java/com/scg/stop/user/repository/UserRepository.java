package com.scg.stop.user.repository;

import com.scg.stop.user.domain.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findBySocialLoginId(String socialLoginId);
}
