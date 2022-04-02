package com.spring.statemachine.domain;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class DomainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_sequence_generator")
    @Column(name = "id")
    private Long id;
    private String guid = UUID.randomUUID().toString();

    @Version

    private Long version;


    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();
}
