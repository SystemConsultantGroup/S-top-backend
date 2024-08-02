package com.scg.stop.user.repository;

import com.scg.stop.user.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    void findByName();
}
