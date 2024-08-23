package com.scg.stop.project.repository;

import com.scg.stop.project.domain.InquiryReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {

    // find by InquiryId
    @Query("select ir from InquiryReply ir where ir.inquiry.id = :inquiryId")
    InquiryReply findByInquiryId(Long inquiryId);


}
