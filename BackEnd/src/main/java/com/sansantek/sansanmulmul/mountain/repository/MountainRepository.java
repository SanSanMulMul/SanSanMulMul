package com.sansantek.sansanmulmul.mountain.repository;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {
    List<Mountain> findByMountainNameContaining(String name);

    Mountain findByMountainId(int mountainId);

    List<Mountain> findByMountainWeatherIn(List<String> weathers);

}
