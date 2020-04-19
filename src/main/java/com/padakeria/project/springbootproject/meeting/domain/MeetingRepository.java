package com.padakeria.project.springbootproject.meeting.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
