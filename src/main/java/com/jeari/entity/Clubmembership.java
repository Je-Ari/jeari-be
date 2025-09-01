package com.jeari.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "clubmembership")
public class Clubmembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id", nullable = false)
    private Integer id;

    @Column(name = "joined_at")
    private LocalDate joinedAt;


    @Enumerated(EnumType.STRING)
    private ClubRole role;


    @Getter
    @Setter
    @Entity
    @Table(name = "joinrequest")
    public static class Joinrequest {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "request_id", nullable = false)
        private Integer id;

        @NotNull
        @Column(name = "answer", nullable = false, length = Integer.MAX_VALUE)
        private String answer;

        @Column(name = "created_at")
        private OffsetDateTime createdAt;
    }
}