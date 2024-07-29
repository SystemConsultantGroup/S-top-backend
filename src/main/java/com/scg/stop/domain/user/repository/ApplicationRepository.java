package com.scg.stop.domain.user.repository;

import com.scg.stop.domain.user.domain.Application;
import com.scg.stop.domain.user.domain.UserType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a WHERE a.user.userType IN :userTypes")
    Page<Application> findByUserTypeIn(@Param("userTypes") List<UserType> userTypes, Pageable pageable);

}
