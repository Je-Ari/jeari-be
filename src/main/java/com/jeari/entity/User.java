package com.jeari.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 10)
    @NotNull
    @Column(name = "student_id", nullable = false, length = 10)
    private String studentId;

    @NotNull
    @Column(name = "pw_hash", nullable = false, length = Integer.MAX_VALUE)
    private String pwHash;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotNull
    @Column(name = "id", nullable = false)
    private Long id1;

    @NotNull
    @Column(name = "password_hash", nullable = false, length = Integer.MAX_VALUE)
    private String passwordHash;
    @Size(max = 10)
    @NotNull
    @Column(name = "role", nullable = false, length = 10)
    private String role;

/*
TODO [리버스 엔지니어링] 필드를 생성하여 'user_role' 열에 매핑
사용 가능한 액션: 대상 Java 타입 정의 | 현재 상태대로 주석 해제 | 열 매핑 제거
    @ColumnDefault("'user'")
    @Column(name = "user_role", columnDefinition = "user_role")
    private Object userRole;
*/
}