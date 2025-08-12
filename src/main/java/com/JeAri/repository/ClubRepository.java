package com.JeAri.repository;

import com.JeAri.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    // 기본 CRUD 메서드 사용 가능
}