package com.jeari.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "club_membership", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"club_id", "user_id"})
})
public class ClubMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "club_id", nullable = false)
    private Integer clubId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "joined_at")
    private LocalDate joinedAt;

    @Column(name = "club_role")
    @Enumerated(EnumType.STRING)
    private ClubRole role;

}