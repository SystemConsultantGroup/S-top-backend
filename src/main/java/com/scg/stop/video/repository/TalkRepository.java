package com.scg.stop.video.repository;

import com.scg.stop.video.domain.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TalkRepository extends JpaRepository<Talk, Long> {

    @Query("SELECT t from Talk t "+
            "WHERE (:year IS NULL OR t.year = :year) " +
            "AND (:title IS NULL OR t.title LIKE %:title%) " +
            "AND ((:isKeynoteSpeech IS NULL AND t.keynoteSpeech = false) OR t.keynoteSpeech = :isKeynoteSpeech) " +
            "ORDER BY t.year DESC")
    Page<Talk> findPages(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("isKeynoteSpeech") Boolean isKeynoteSpeech,
            Pageable pageable
    );
}
