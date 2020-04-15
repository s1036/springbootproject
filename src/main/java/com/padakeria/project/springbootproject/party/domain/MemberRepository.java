package com.padakeria.project.springbootproject.party.domain;

import com.padakeria.project.springbootproject.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByPartyAndAccount(Party party, Account account);
}
