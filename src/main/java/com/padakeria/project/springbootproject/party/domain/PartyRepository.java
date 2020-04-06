package com.padakeria.project.springbootproject.party.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface PartyRepository extends JpaRepository<Party, Long> {
}
