package com.sansantek.sansanmulmul.stone.repository;

import com.sansantek.sansanmulmul.stone.domain.UserSummitstone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSummitstoneRepository extends JpaRepository<UserSummitstone, Integer> {
    List<UserSummitstone> findByUser_UserId(int userId);
    boolean existsByUserUserIdAndSummitstoneStoneId(int userId, int summitstoneId);
}