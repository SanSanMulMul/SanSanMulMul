package com.sansantek.sansanmulmul.stone.repository;

import com.sansantek.sansanmulmul.stone.domain.Summitstone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SummitstoneRepository extends JpaRepository<Summitstone, Integer> {
    Optional<Summitstone> findByStoneId(int stoneId);
}
