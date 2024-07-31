package com.sansantek.sansanmulmul.mountain.repository;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {
}
