package com.sansantek.sansanmulmul.mountain.repository;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {
    List<Mountain> findByMountainNameContaining(String name);

    Optional<Mountain> findByMountainId(int mountainId);
    Mountain findByMountainName(String mountainName);
    Mountain findByMountainCode(int mountainCode);

    List<Mountain> findByMountainWeatherIn(List<String> weathers);

}
