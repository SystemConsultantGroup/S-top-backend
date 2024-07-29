package com.scg.stop.domain.user.repository;

import com.scg.stop.domain.user.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
