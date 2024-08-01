package com.sansantek.sansanmulmul.mountain.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
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
    private int mountainId;

    @Column(name = "mountain_code")
    private int mountainCode;

    @Column(name = "mountain_name")
    private String mountainName;

    @Column(name= "mountain_location")
    private String mountainLocation;

    @Column(name = "mountain_height")
    private int mountainHeight;

    @Column(name = "mountain_description",columnDefinition = "TEXT")
    private String mountainDescription;

    @Column(name = "mountain_img")
    private String mountainImg;

    @Column(name = "mountain_weather")
    private String mountainWeather;

    @Column(name = "mountain_lat")
    private double mountainLat; //위도

    @Column(name = "mountain_lon")
    private double mountainLon; //경도

    @OneToMany(mappedBy = "mountain")
    @JsonManagedReference
    private List<MountainSpot> mountainSpots;

}
