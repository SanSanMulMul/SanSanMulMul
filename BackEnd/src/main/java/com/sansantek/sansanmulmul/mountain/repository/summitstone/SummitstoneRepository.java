package com.sansantek.sansanmulmul.mountain.repository.summitstone;

import com.sansantek.sansanmulmul.mountain.domain.summitstone.Summitstone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SummitstoneRepository extends JpaRepository<Summitstone, Integer> {
    Optional<Summitstone> findByStoneId(int stoneId);
}
