package com.sansantek.sansanmulmul.mountain.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Mountain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mountain_id")
    @Schema(description = "산 고유 번호", example = "1")
    private int mountainId;

    @Column(name = "mountain_code")
    @Schema(description = "산 고유 코드", example = "4130612")
    private int mountainCode;

    @Column(name = "mountain_name")
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;

    @Column(name= "mountain_location")
    @Schema(description = "산 지역", example = "경상북도 구미시")
    private String mountainLocation;

    @Column(name = "mountain_height")
    @Schema(description = "산 높이", example = "1257")
    private int mountainHeight;

    @Column(name = "mountain_description",columnDefinition = "TEXT")
    @Schema(description = "산 상세 설명", example = "금오산은 구미를 대표하는 산중하나이다.")
    private String mountainDescription;

    @Column(name = "mountain_img")
    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;

    @Column(name = "mountain_weather")
    @Schema(description = "계절명산" ,example = "SUMMER")
    private String mountainWeather;

    @Column(name = "mountain_lat")
    @Schema(description = "산 위도",example = "35.405006")
    private double mountainLat; //위도

    @Column(name = "mountain_lon")
    @Schema(description = "산 경도", example = "124.123456")
    private double mountainLon; //경도

    @OneToMany(mappedBy = "mountain")
    @JsonManagedReference
    private List<MountainSpot> mountainSpots;

    // 그룹이 선택한 산
//    @OneToMany(mappedBy = "mountain")
//    private List<Crew> crews;

}
