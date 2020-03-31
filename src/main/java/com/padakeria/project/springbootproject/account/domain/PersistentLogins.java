package com.padakeria.project.springbootproject.account.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "persistent_logins")
@Entity
@Getter
public class PersistentLogins {

    @Id
    @Column(name = "series", length = 64)
    private String series;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "token", length = 64, nullable = false)
    private String token;

    @Column(name = "last_used", length = 64, nullable = false)
    private LocalDateTime lastUsed;

}
///create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, "
//			+ "token varchar(64) not null, last_used timestamp not null)