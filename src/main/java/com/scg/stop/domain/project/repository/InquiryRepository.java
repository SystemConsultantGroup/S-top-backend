package com.scg.stop.domain.project.repository;

import com.scg.stop.domain.project.domain.Inquiry;
import com.scg.stop.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUser(User user);
}