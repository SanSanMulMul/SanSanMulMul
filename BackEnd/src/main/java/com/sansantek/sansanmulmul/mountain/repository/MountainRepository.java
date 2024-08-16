package com.sansantek.sansanmulmul.mountain.repository;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT DISTINCT m FROM Mountain m " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(m.mountainLat)) * " +
            "cos(radians(m.mountainLon) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(m.mountainLat)))) <= :upToKm")
    List<Mountain> findByGeo(@Param("latitude") Double latitude,
                             @Param("longitude") Double longitude,
                             @Param("upToKm") Double upToKm);
}
