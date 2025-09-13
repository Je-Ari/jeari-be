package com.jeari.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "club")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE club SET is_deleted = true WHERE club_id = ?")     // soft delete
@SQLRestriction("is_deleted = false")                                       // 쿼리에 where절 추가
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClubCategory category;

    @Column(length = 15)
    private String subcategory;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Builder
    public Club(String name, String description, ClubCategory category, String subcategory) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
    }

}
