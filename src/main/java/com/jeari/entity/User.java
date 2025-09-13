package com.jeari.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE \"user\" SET is_deleted = true WHERE user_id = ?")     // user는 db 예약어 취급을 받을 수도 있어서 큰따옴표 처리
@SQLRestriction("is_deleted = false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(nullable = false, length = 50)
    private String name;

    @Size(max = 10)
    @NotNull
    @Column(name = "student_id", nullable = false, length = 10, unique = true)
    private String studentId;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Size(max = 100)
    @NotNull
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 20)
    private UserRole userRole = UserRole.USER;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
