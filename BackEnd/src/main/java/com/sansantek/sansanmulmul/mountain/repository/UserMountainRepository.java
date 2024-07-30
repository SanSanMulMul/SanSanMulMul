package com.sansantek.sansanmulmul.mountain.repository;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.UserMountain;
import com.sansantek.sansanmulmul.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserMountainRepository extends JpaRepository<UserMountain, Long> {
    Optional<UserMountain> findByUserAndMountain(User user, Mountain mountain);
    @Query("SELECT um.mountain FROM UserMountain um WHERE um.user = :user")
    List<Mountain> findMountainsByUser(@Param("user") User user);
}
