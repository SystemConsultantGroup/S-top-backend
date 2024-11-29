package com.scg.stop.project.repository;

import com.scg.stop.project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
