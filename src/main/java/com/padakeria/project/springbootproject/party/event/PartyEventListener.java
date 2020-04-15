package com.padakeria.project.springbootproject.party.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Async
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PartyEventListener {

}
