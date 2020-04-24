package com.padakeria.project.springbootproject.meeting.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Page<Meeting> findByParty(Pageable realPage);
}
