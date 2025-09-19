package com.jeari.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;


@Getter
@Setter
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE \"users\" SET is_deleted = true WHERE user_id = ?")     // user는 db 예약어 취급을 받을 수도 있어서 큰따옴표 처리
@SQLRestriction("is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "user_role", nullable = false, length = 20)
    private UserRole userRole = UserRole.ROLE_USER;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
