package com.sansantek.sansanmulmul.user.repository.summitstone;

import com.sansantek.sansanmulmul.user.domain.summitstone.UserSummitstone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSummitstoneRepository extends JpaRepository<UserSummitstone, Integer> {
    List<UserSummitstone> findByUser_UserId(int userId);
}