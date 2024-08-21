package com.scg.stop.domain.file.repository;

import com.scg.stop.domain.file.domain.File;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findById(Long id);
    List<File> findByIdIn(List<Long> ids);
}