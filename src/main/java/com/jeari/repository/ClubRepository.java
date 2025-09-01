package com.jeari.repository;

import com.jeari.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    // 기본 CRUD 메서드 사용 가능

}