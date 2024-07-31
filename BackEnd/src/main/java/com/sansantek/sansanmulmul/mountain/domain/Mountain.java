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
    private int mountain_id;

    private int mountain_code;

    @Column(name = "mountain_name")
    private String mountainName;

    private String mountain_location;

    private int mountain_height;

    @Column(columnDefinition = "TEXT")
    private String mountain_description;

    private String mountain_img;

    private String mountain_weather;

    private double mountain_lat; //위도
    
    private double mountain_lon; //경도

    @OneToMany(mappedBy = "mountain")
    @JsonManagedReference
    private List<MountainSpot> mountainSpots;

}
