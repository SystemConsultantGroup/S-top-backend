package com.scg.stop.file.repository;

import com.scg.stop.file.domain.File;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByIdIn(List<Long> ids);
}
