package com.scg.stop.user.repository;

import com.scg.stop.user.domain.Application;
import com.scg.stop.user.domain.ApplicationStatus;
import com.scg.stop.user.domain.UserType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByStatus(ApplicationStatus status, Pageable pageable);
}
