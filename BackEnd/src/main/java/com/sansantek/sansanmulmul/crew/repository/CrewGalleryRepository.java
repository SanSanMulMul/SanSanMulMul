package com.sansantek.sansanmulmul.crew.repository;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewgallery.CrewGallery;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewGalleryRepository extends JpaRepository<CrewGallery, Integer> {
    List<CrewGallery> findByCrew(Crew crew);
    CrewGallery findByPictureId(Integer pictureId);
}

