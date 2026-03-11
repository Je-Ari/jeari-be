package com.jeari.repository;

import com.jeari.entity.JoinRequest;
import com.jeari.entity.Recruitment;
import com.jeari.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Integer> {
    Optional<JoinRequest> findByRecruitmentAndUser(Recruitment recruitment, User user);
}
