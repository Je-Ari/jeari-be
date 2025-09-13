package com.jeari.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE post_id = ?")     // delete명령을 소프트 delete로 변경해줌
@SQLRestriction("is_deleted = false")                                       // 쿼리에 where절을 추가해줌
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "clubmembership_id", nullable = false)
    private Integer clubmembershipId;

    @Size(max = 50)
    @NotNull
    @Column(nullable = false, length = 50)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ColumnDefault("0")
    private Integer view;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}